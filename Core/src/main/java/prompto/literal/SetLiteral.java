package prompto.literal;

import java.util.Collection;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.intrinsic.PromptoSet;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.MissingType;
import prompto.type.SetType;
import prompto.utils.CodeWriter;
import prompto.utils.ExpressionList;
import prompto.value.IValue;
import prompto.value.SetValue;

public class SetLiteral extends ContainerLiteral<SetValue> {

	public SetLiteral() {
		super(()->"<>", new SetValue(MissingType.instance()), null, false);
	}

	public SetLiteral(ExpressionList expressions) {
		super(()->"<" + expressions.toString() + ">", new SetValue(MissingType.instance()), expressions, false);
	}

	@Override
	protected Collection<IValue> getItems() {
		return value.getItems();
	}
	
	@Override
	protected IType newType(IType itemType) {
		return new SetType(itemType);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(value.isEmpty() && expressions!=null && !expressions.isEmpty()) {
			check(context); // force computation of itemType
			PromptoSet<IValue> set = new PromptoSet<IValue>();
			for(IExpression exp : expressions) {
				IValue item = exp.interpret(context);
				item = interpretPromotion(item);
				set.add(item);
			}
			value = new SetValue(itemType, set);
			// don't dispose of expressions, they are required by translation 
		}
		return value;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(expressions!=null) {
			writer.append('<');
			expressions.toDialect(writer);
			writer.append('>');
		} else
			writer.append("< >");
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = CompilerUtils.compileNewInstance(method, PromptoSet.class);
		if(expressions!=null)
			compileItems(context, method, PromptoSet.class);
		return info;
	}

	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("StrictSet");
	    this.expressions.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("new StrictSet([");
	    this.expressions.transpile(transpiler);
	    transpiler.append("])");
	    return false;
	}
}
