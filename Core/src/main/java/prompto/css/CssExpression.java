package prompto.css;

import java.util.ArrayList;
import java.util.List;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CssType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.CssValue;
import prompto.value.IValue;

public class CssExpression implements IExpression {

	List<CssField> fields = new ArrayList<>();
	
	@Override
	public IType check(Context context) {
		return CssType.instance();
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		return new CssValue(this);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("{");
		fields.forEach(field->field.toDialect(writer));
		writer.append("}");
	}

	public void addField(CssField field) {
		fields.add(field);
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		return CompilerUtils.compileNewInstance(method, Object.class);
	}

	@Override
	public void declare(Transpiler transpiler) {
		fields.forEach(field->field.declare(transpiler));
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("{");
		fields.forEach(field->{
			field.transpile(transpiler);
			transpiler.append(", ");
		});
		transpiler.trimLast(", ".length());
		transpiler.append("}");
		return false;
	}

}
