package prompto.type;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.Map;

import prompto.expression.IExpression;
import prompto.grammar.CmpOp;
import prompto.grammar.Identifier;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.Character;
import prompto.value.CharacterRange;
import prompto.value.IValue;
import prompto.value.RangeBase;

import com.fasterxml.jackson.databind.JsonNode;

public class CharacterType extends NativeType {

	static CharacterType instance = new CharacterType();

	public static CharacterType instance() {
		return instance;
	}

	private CharacterType() {
		super(Family.CHARACTER);
	}

	@Override
	public Type getJavaType(Context context) {
		return java.lang.Character.class;
	}

	@Override
	public IType checkAdd(Context context, IType other, boolean tryReverse) {
		return TextType.instance();
	}

	@Override
	public IType checkMultiply(Context context, IType other, boolean tryReverse) {
		if (other instanceof IntegerType)
			return TextType.instance();
		else
			return super.checkMultiply(context, other, tryReverse);
	}

	@Override
	public IType checkCompare(Context context, IType other, ISection section) {
		if (other instanceof CharacterType || other instanceof TextType)
			return BooleanType.instance();
		return super.checkCompare(context, other, section);
	}

	@Override
	public IType checkMember(Context context, Identifier id) {
		String name = id.toString();
		if ("codePoint".equals(name))
			return IntegerType.instance();
		else
			return super.checkMember(context, id);
	}

	@Override
	public IType checkRange(Context context, IType other) {
		if (other instanceof CharacterType)
			return new RangeType(this);
		return super.checkRange(context, other);
	}

	@Override
	public RangeBase<?> newRange(Object left, Object right) {
		if (left instanceof Character && right instanceof Character)
			return new CharacterRange((Character) left, (Character) right);
		return super.newRange(left, right);
	}

	@Override
	public Comparator<Character> getComparator(boolean descending) {
		return descending ? new Comparator<Character>() {
			@Override
			public int compare(Character o1, Character o2) {
				return java.lang.Character.compare(o2.getValue(), o1.getValue());
			}
		} : new Comparator<Character>() {
			@Override
			public int compare(Character o1, Character o2) {
				return java.lang.Character.compare(o1.getValue(), o2.getValue());
			}
		};
	}

	@Override
	public String toString(Object value) {
		return "'" + value.toString() + "'";
	}

	@Override
	public IValue convertJavaValueToIValue(Context context, Object value) {
		if (value instanceof java.lang.Character)
			return new Character(((java.lang.Character) value).charValue());
		else
			return (IValue) value; // TODO for now
	}

	@Override
	public IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts) {
		if (value.asText().length() > 1)
			throw new InvalidParameterException(value.toString());
		return new Character(value.asText().charAt(0));
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}

	@Override
	public void declareAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
		// can add anything to text
		left.declare(transpiler);
		right.declare(transpiler);
	}

	@Override
	public void transpileAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
		// can add anything to text
		left.transpile(transpiler);
		transpiler.append(" + ");
		right.transpile(transpiler);
	}
	
	@Override
	public void declareMultiply(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if (other == IntegerType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else
	        super.declareMultiply(transpiler, other, tryReverse, left, right);
	}
	
	@Override
	public void transpileMultiply(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	   if (other == IntegerType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".repeat(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        super.transpileMultiply(transpiler, other, tryReverse, left, right);
	}
	
	@Override
	public void declareMember(Transpiler transpiler, String name) {
	    if (!"codePoint".equals(name))
	    	super.declareMember(transpiler, name);
	}
	
	@Override
	public void transpileMember(Transpiler transpiler, String name) {
	    if ("codePoint".equals(name))
	    	transpiler.append("charCodeAt(0)");
	    else
	    	super.transpileMember(transpiler, name);
	}

	@Override
	public void declareRange(Transpiler transpiler, IType other) {
	    if(other == CharacterType.instance()) {
	        transpiler.require("Range");
	        transpiler.require("IntegerRange");
	        transpiler.require("CharacterRange");
	    } else {
	        super.declareRange(transpiler, other);
	    }
	}
	
	
	@Override
	public void transpileRange(Transpiler transpiler, IExpression first, IExpression last) {
	    transpiler.append("new CharacterRange(");
	    first.transpile(transpiler);
	    transpiler.append(",");
	    last.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void declareCompare(Transpiler transpiler, IType other) {
		// nothing to do
	}
	
	@Override
	public void transpileCompare(Transpiler transpiler, IType other, CmpOp operator, IExpression left, IExpression right) {
	    left.transpile(transpiler);
	    transpiler.append(" ").append(operator.toString()).append(" ");
	    right.transpile(transpiler);
	}
}
