package prompto.value;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.Map;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.ShortOperand;
import prompto.compiler.StackState;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.CmpOp;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoChar;
import prompto.runtime.Context;
import prompto.type.CharacterType;

import com.fasterxml.jackson.core.JsonGenerator;

public class Character extends BaseValue implements Comparable<Character>, IMultiplyable
{
    char value;

    public Character(char value)
    {
		super(CharacterType.instance());
        this.value = value;
    }

    public char getValue() { 
    	return value; 
    }
    
    @Override
    public Object getStorableData() {
    	return value;
    }
    
    public IValue asText() {
    	return new Text(java.lang.Character.toString(value));
    }

    @Override
    public IValue plus(Context context, IValue value) {
        return new Text(this.value + value.toString());
    }

	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression right) {
		// convert to String
		MethodConstant c = new MethodConstant(java.lang.Character.class, 
									"toString", 
									String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, c);
		// use Text::compileAdd
		return Text.compilePlus(context, method, flags, left, right);
	}
	
    @Override
    public IValue multiply(Context context, IValue value) throws PromptoError {
        if (value instanceof Integer) {
            int count = (int)((Integer)value).longValue();
            if (count < 0)
                throw new SyntaxError("Negative repeat count:" + count);
            return new Text(PromptoChar.multiply(this.value, count));
      } else
           throw new SyntaxError("Illegal: Chararacter * " + value.getClass().getSimpleName());
     }

	public static ResultInfo compileMultiply(Context context, MethodInfo method, Flags flags, ResultInfo left, IExpression exp) {
		CompilerUtils.CharacterTochar(method);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		if(Long.class==right.getType())
			CompilerUtils.LongToint(method);
		else if(long.class==right.getType())
			CompilerUtils.longToint(method);
		MethodConstant oper = new MethodConstant(PromptoChar.class, 
				"multiply", 
				char.class, int.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(String.class);
	}
	
    public int compareTo(Character obj) {
        return java.lang.Character.compare(value, obj.value);
    }

    @Override
    public int compareTo(Context context, IValue value) {
        if (value instanceof Character)
            return java.lang.Character.compare(this.value, ((Character)value).value);
        else
            throw new SyntaxError("Illegal comparison: Character + " + value.getClass().getSimpleName());
    }
    
    @Override
    public IValue getMember(Context context, Identifier id, boolean autoCreate) throws PromptoError {
		if ("codePoint".equals(id.toString()))
			return new Integer((int)value);
		else
			return super.getMember(context, id, autoCreate);
    };

    static Opcode[] cmpOpcodes = createOpcodes();
    
    private static Opcode[] createOpcodes() {
    	Opcode[] opcodes = new Opcode[CmpOp.values().length];
    	opcodes[CmpOp.LT.ordinal()] = Opcode.IF_ICMPLT;
    	opcodes[CmpOp.LTE.ordinal()] = Opcode.IF_ICMPLE;
    	opcodes[CmpOp.GT.ordinal()] = Opcode.IF_ICMPGT;
    	opcodes[CmpOp.GTE.ordinal()] = Opcode.IF_ICMPGE;
 		return opcodes;
 	}
    
	public static ResultInfo compileCompareTo(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		if(java.lang.Character.class==left.getType())
			CompilerUtils.CharacterTochar(method);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		if(java.lang.Character.class==right.getType())
			CompilerUtils.CharacterTochar(method);
		Opcode opcode = cmpOpcodes[flags.cmpOp().ordinal()];
		method.addInstruction(opcode, new ShortOperand((short)7));
		StackState branchState = method.captureStackState();
		method.addInstruction(Opcode.ICONST_0);
		method.addInstruction(Opcode.GOTO, new ShortOperand((short)4));
		method.restoreFullStackState(branchState);
		method.placeLabel(branchState);
		method.addInstruction(Opcode.ICONST_1);
		StackState lastState = method.captureStackState();
		method.placeLabel(lastState);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}

	@Override
	public Object convertTo(Context context, Type type) {
        return value;
    }
    
    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Character)
            return value == ((Character)obj).value;
        else
            return false;
    }
    
	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, ResultInfo left, IExpression exp) {
		if(java.lang.Character.class==left.getType())
			CompilerUtils.CharacterTochar(method);
		ResultInfo right = exp.compile(context, method, flags);
		if(java.lang.Character.class==right.getType())
			CompilerUtils.CharacterTochar(method);
		Opcode opcode = flags.isReverse() ? Opcode.IF_ICMPNE : Opcode.IF_ICMPEQ;
		method.addInstruction(opcode, new ShortOperand((short)7));
		StackState branchState = method.captureStackState();
		method.addInstruction(Opcode.ICONST_0);
		method.addInstruction(Opcode.GOTO, new ShortOperand((short)4));
		method.restoreFullStackState(branchState);
		method.placeLabel(branchState);
		method.addInstruction(Opcode.ICONST_1);
		StackState lastState = method.captureStackState();
		method.placeLabel(lastState);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
    
    @Override
    public boolean roughly(Context context, IValue obj) throws PromptoError {
        if (obj instanceof Character || obj instanceof Text) {
        	Collator c = Collator.getInstance();
        	c.setStrength(Collator.PRIMARY);
        	return c.compare(value, obj.toString())==0;
        } else
            return false;
    }
    
   @Override
   public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				generator.writeString(CharacterType.instance().getTypeName());
				generator.writeFieldName("value");
				generator.writeString("" + value);
				generator.writeEndObject();
			} else
				generator.writeString("" + value);
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
   }



}

