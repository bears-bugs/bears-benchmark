package prompto.statement;

import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.literal.ContainerLiteral;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.ContainerType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.utils.ExpressionList;
import prompto.value.IContainer;
import prompto.value.IValue;
import prompto.value.ListValue;


public class CollectionSwitchCase extends SwitchCase {

	public CollectionSwitchCase(IExpression expression, StatementList list) {
		super(expression, list);
	}

	@Override
	public void checkSwitchType(Context context, IType type) {
		IType thisType = expression.check(context);
		if(thisType instanceof ContainerType)
			thisType = ((ContainerType)thisType).getItemType();
		if(!type.isAssignableFrom(context, thisType))
			throw new SyntaxError("Cannot assign:" + thisType.getTypeName() + " to:" + type.getTypeName());
	}
	
	@Override
	public boolean matches(Context context, IValue value) throws PromptoError {
		Object thisValue = expression.interpret(context);
		if(thisValue instanceof IContainer)
			return ((IContainer<?>)thisValue).hasItem(context, value);
		else if(thisValue instanceof ListValue)
			return ((ListValue)thisValue).hasItem(context, value);
		return false;
	}
	
	@Override
	public void caseToPDialect(CodeWriter writer) {
		caseToEDialect(writer);
	}
	
	@Override
	public void caseToODialect(CodeWriter writer) {
		writer.append("case in ");
		expression.toDialect(writer);
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}
	
	@Override
	public void caseToEDialect(CodeWriter writer) {
		writer.append("when in ");
		expression.toDialect(writer);
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}
	
	@Override
	public void catchToODialect(CodeWriter writer) {
		writer.append("catch (");
		expression.toDialect(writer);
		writer.append(") {\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("} ");
	}
	
	@Override
	public void catchToPDialect(CodeWriter writer) {
		writer.append("except in ");
		expression.toDialect(writer);
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}
	
	@Override
	public void catchToEDialect(CodeWriter writer) {
		caseToEDialect(writer); // no difference
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		ExpressionList expressions = ((ContainerLiteral<?>)this.expression).getExpressions();
		expressions.forEach(expression -> {
	        transpiler.append("case ");
	        expression.transpile(transpiler);
	        transpiler.append(":").newLine();
	    });
	    transpiler.indent(true);
	    this.statements.transpile(transpiler);
	    transpiler.append("break;").dedent();
	}
	
	@Override
	public void transpileError(Transpiler transpiler) {
		throw new UnsupportedOperationException();
	}
}
