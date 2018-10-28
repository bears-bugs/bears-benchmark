package prompto.literal;

import java.util.Collection;
import java.util.function.Supplier;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.type.CharacterType;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.TextType;
import prompto.utils.ExpressionList;
import prompto.utils.TypeUtils;
import prompto.value.Character;
import prompto.value.Decimal;
import prompto.value.IContainer;
import prompto.value.IValue;
import prompto.value.Integer;

public abstract class ContainerLiteral<T extends IContainer<IValue>> extends Literal<T> {

	boolean mutable;
	IType itemType = null;
	ExpressionList expressions = null;
	
	protected ContainerLiteral(Supplier<String> text, T value, ExpressionList expressions, boolean mutable) {
		super(text, value);
		this.expressions = expressions;
		this.mutable = mutable;
	}

	public boolean isMutable() {
		return mutable;
	}

	@Override
	public IType check(Context context) {
		if(itemType==null) {
			if(expressions!=null)
				itemType = TypeUtils.inferElementType(context, expressions);
			else
				itemType = TypeUtils.inferValuesType(context, getItems());
		}
		return newType(itemType); 
	}

	protected abstract IType newType(IType itemType);

	protected abstract Collection<IValue> getItems();

	protected IValue interpretPromotion(IValue item) {
		if(item==null)
			return item;
		if(DecimalType.instance()==itemType && item.getType()==IntegerType.instance())
			return new Decimal(((Integer)item).doubleValue());
		else if(TextType.instance()==itemType && item.getType()==CharacterType.instance())
			return ((Character)item).asText();
		else
			return item;
	}

	public ExpressionList getExpressions() {
		return expressions;
	}

	protected void compileItems(Context context, MethodInfo method, Class<?> klass) {
		Flags flags = new Flags();
		flags.withPrimitive(true);
		for(IExpression e : expressions) {
			method.addInstruction(Opcode.DUP); // need to keep a reference to the list on top of stack
			ResultInfo info = e.compile(context, method, flags);
			compilePromotion(method, info);
			IOperand c = new MethodConstant(klass, "add", 
					Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, c);
			method.addInstruction(Opcode.POP); // consume the returned boolean
		}
	}
	
	protected ResultInfo compilePromotion(MethodInfo method, ResultInfo info) {
		if(DecimalType.instance()==itemType && Long.class==info.getType())
			return CompilerUtils.LongToDouble(method);
		else if(TextType.instance()==itemType) {
			if(char.class==info.getType())
				return CompilerUtils.charToString(method);
			else if(java.lang.Character.class==info.getType())
				return CompilerUtils.CharacterToString(method);
		}
		return info;
	}
	

	
}
