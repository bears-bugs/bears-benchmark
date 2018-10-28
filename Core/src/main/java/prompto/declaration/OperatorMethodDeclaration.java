package prompto.declaration;

import prompto.argument.IArgument;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.grammar.Operator;
import prompto.runtime.Context;
import prompto.statement.StatementList;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;

public class OperatorMethodDeclaration extends ConcreteMethodDeclaration implements IExpression, IMethodDeclaration {
	
	Operator operator;
	
	public OperatorMethodDeclaration(Operator op, IArgument arg, IType returnType, StatementList stmts) {
		super(new Identifier(getNameAsKey(op)), new ArgumentList(arg), returnType, stmts);
		this.operator = op;
	}

	public static String getNameAsKey(Operator operator) {
		return "operator_" + operator.name();
	}
	
	@Override
	public String getNameAsKey() {
		return getNameAsKey(operator);
	}

	@Override
	public IType check(Context context) {
		// called as IExpression::check
		return check(context, false);
	}
	
	@Override
	public void check(ConcreteCategoryDeclaration declaration, Context context) {
		// TODO Auto-generated method stub
		
	}	

	protected void toMDialect(CodeWriter writer) {
		writer.append("def operator ");
		writer.append(operator.getToken());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(")");
		if(returnType!=null && returnType!=VoidType.instance()) {
			writer.append("->");
			returnType.toDialect(writer);
		}
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}

	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(operator.getToken());
		writer.append(" as operator ");
		arguments.toDialect(writer);
		if(returnType!=null && returnType!=VoidType.instance()) {
			writer.append("returning ");
			returnType.toDialect(writer);
			writer.append(" ");
		}
		writer.append("doing:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}
	
	protected void toODialect(CodeWriter writer) {
		if(returnType!=null && returnType!=VoidType.instance()) {
			returnType.toDialect(writer);
			writer.append(" ");
		}
		writer.append("operator ");
		writer.append(operator.getToken());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(") {\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("}\n");
	}
	
}
