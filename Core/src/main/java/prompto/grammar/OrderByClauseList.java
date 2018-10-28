package prompto.grammar;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.store.IQueryBuilder;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.utils.ObjectList;

@SuppressWarnings("serial")
public class OrderByClauseList extends ObjectList<OrderByClause> {

	public OrderByClauseList() {	
	}
	
	public OrderByClauseList(OrderByClause clause) {
		this.add(clause);
	}

	public void toDialect(CodeWriter writer) {
		writer.append("order by ");
		if(writer.getDialect()==Dialect.O)
			writer.append("( ");
		for(OrderByClause clause : this) {
			clause.toDialect(writer);
			writer.append(", ");
		}
		writer.trimLast(2);
		if(writer.getDialect()==Dialect.O)
			writer.append(" )");
	}

	public void interpretQuery(Context context, IQueryBuilder q) {
		this.forEach((clause)->
			clause.interpretQuery(context, q));
	}

	public void compileQuery(Context context, MethodInfo method, Flags flags) {
		this.forEach((clause)->
			clause.compileQuery(context, method, flags));
	}

	public void declare(Transpiler transpiler) {
	    this.forEach(clause -> clause.declare(transpiler));
	}

	public void transpileQuery(Transpiler transpiler, String builderName) {
		this.forEach(clause -> clause.transpileQuery(transpiler, builderName));
	}

}
