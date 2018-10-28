package prompto.statement;

import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.parser.ISection;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public abstract class SwitchCase extends Section implements ISection {

	protected IExpression expression;
	StatementList statements;
	
	public SwitchCase(IExpression expression, StatementList statements) {
		this.expression = expression;
		this.statements = statements;
	}
	
	public IExpression getExpression() {
		return expression;
	}

	public abstract void checkSwitchType(Context context, IType type);

	public IType checkReturnType(Context context) {
		return statements.check(context, null);
	}

	public abstract boolean matches(Context context, IValue value) throws PromptoError;

	public IValue interpret(Context context) throws PromptoError {
		return statements.interpret(context);
	}

	public abstract void caseToEDialect(CodeWriter writer);
	public abstract void caseToODialect(CodeWriter writer);
	public abstract void caseToPDialect(CodeWriter writer);
	public abstract void catchToEDialect(CodeWriter writer);
	public abstract void catchToODialect(CodeWriter writer);
	public abstract void catchToPDialect(CodeWriter writer);

	public void declare(Transpiler transpiler) {
	    if(this.expression!=null)
	        this.expression.declare(transpiler);
	    this.statements.declare(transpiler);
	}

	public abstract void transpile(Transpiler transpiler);
	public abstract void transpileError(Transpiler transpiler);


}
