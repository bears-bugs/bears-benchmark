package prompto.literal;

import java.util.Collection;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.intrinsic.PromptoList;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.ListType;
import prompto.type.MissingType;
import prompto.utils.CodeWriter;
import prompto.utils.ExpressionList;
import prompto.value.IValue;
import prompto.value.ListValue;

public class ListLiteral extends ContainerLiteral<ListValue> {

	private static String getText(ExpressionList expressions, boolean mutable) {
		return (mutable ? "mutable " : "") 
				+ (expressions==null ? "[]" : "[" + expressions.toString() + "]");
	}
	
	public ListLiteral(boolean mutable) {
		super(()->getText(null, mutable), new ListValue(MissingType.instance()), null, mutable);
	}
	
	public ListLiteral(ExpressionList expressions, boolean mutable) {
		super(()->getText(expressions, mutable), new ListValue(MissingType.instance()), expressions, mutable);
	}
	
	@Override
	protected Collection<IValue> getItems() {
		return value.getItems();
	}
	
	@Override
	protected IType newType(IType itemType) {
		return new ListType(itemType);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(expressions!=null) {
			check(context); // force computation of itemType
			PromptoList<IValue> list = new PromptoList<IValue>(mutable);
			for(IExpression exp : expressions) {
				IValue item = exp.interpret(context);
				item = interpretPromotion(item);
				list.add(item);
			}
			return new ListValue(itemType, list);
		} else
			return value;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(mutable)
			writer.append("mutable ");
		if(expressions!=null) {
			writer.append('[');
			expressions.toDialect(writer);
			writer.append(']');
		} else
			writer.append("[]");
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = CompilerUtils.compileNewRawInstance(method, PromptoList.class);
		method.addInstruction(Opcode.DUP);
		method.addInstruction(mutable ? Opcode.ICONST_1 : Opcode.ICONST_0);
		CompilerUtils.compileCallConstructor(method, PromptoList.class, boolean.class);
		if(expressions!=null)
			compileItems(context, method, PromptoList.class);
		return info;
	}

	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("List");
	    if(this.expressions!=null)
	        this.expressions.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	   transpiler.append("new List(").append(this.mutable).append(", [");
	    if(this.expressions!=null) {
	        this.expressions.transpile(transpiler);
	        transpiler.append("])");
	    } else
	        transpiler.append("])");
	    return false;
	}
}
