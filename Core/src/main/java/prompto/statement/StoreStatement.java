package prompto.statement;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.intrinsic.PromptoStoreQuery;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.utils.ExpressionList;
import prompto.value.IValue;

public class StoreStatement extends SimpleStatement {
	
	ExpressionList deletables;
	ExpressionList storables;
	
	public StoreStatement(ExpressionList delete, ExpressionList add) {
		this.deletables = delete;
		this.storables = add;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(deletables!=null) {
			writer.append("delete ");
			if(writer.getDialect()==Dialect.E)
				deletables.toDialect(writer);
			else {
				writer.append('(');
				deletables.toDialect(writer);
				writer.append(')');
			}
			if(storables!=null)
				writer.append(" and ");
		}
		if(storables!=null) {
			writer.append("store ");
			if(writer.getDialect()==Dialect.E)
				storables.toDialect(writer);
			else {
				writer.append('(');
				storables.toDialect(writer);
				writer.append(')');
			}
		}
	}
	
	@Override
	public String toString() {
		return "store " + storables.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof StoreStatement))
			return false;
		StoreStatement other = (StoreStatement)obj;
		return this.storables.equals(other.storables);
	}
	
	@Override
	public IType check(Context context) {
		// TODO check expressions
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		PromptoStoreQuery query = new PromptoStoreQuery();
		if(deletables!=null) for(IExpression exp : deletables) {
			IValue value = exp.interpret(context);
			query.delete(context, value);
		}
		if(storables!=null) for(IExpression exp : storables) {
			IValue value = exp.interpret(context);
			query.store(context, value);
		}
		query.execute();
		return null;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		CompilerUtils.compileNewInstance(method, PromptoStoreQuery.class);
		compileObjectsToDelete(context, method, flags);
		compileStorablesToStore(context, method, flags);
		compileExecute(context, method, flags);
		return new ResultInfo(void.class);
	}

	private void compileStorablesToStore(Context context, MethodInfo method, Flags flags) {
		if(storables!=null) {
			MethodConstant m = new MethodConstant(PromptoStoreQuery.class, "store", Object.class, void.class);
			for(IExpression exp : storables) {
				method.addInstruction(Opcode.DUP);
				exp.compile(context, method, flags);
				method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			}
		}
	}
	
	private void compileObjectsToDelete(Context context, MethodInfo method, Flags flags) {
		if(deletables!=null) {
			MethodConstant m = new MethodConstant(PromptoStoreQuery.class, "delete", Object.class, void.class);
			for(IExpression exp : deletables) {
				method.addInstruction(Opcode.DUP);
				exp.compile(context, method, flags);
				method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			}
		}
	}

	private void compileExecute(Context context, MethodInfo method, Flags flags) {
		MethodConstant m = new MethodConstant(PromptoStoreQuery.class, "execute", void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
	}
	
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("DataStore");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("DataStore.instance.store(");
	    this.transpileIdsToDelete(transpiler);
	    transpiler.append(", ");
	    this.transpileStorablesToAdd(transpiler);
	    transpiler.append(")");
	    return false;
	}

	private void transpileStorablesToAdd(Transpiler transpiler) {
	    if (this.storables==null)
	        transpiler.append("null");
	    else {
	        transpiler.append("(function() { ").indent();
	        transpiler.append("var storablesToAdd = new Set();").newLine();
	        this.storables.forEach(exp-> {
	            exp.transpile(transpiler);
	            transpiler.append(".collectStorables(storablesToAdd);").newLine();
	        });
	        transpiler.append("return storablesToAdd;").newLine();
	        transpiler.dedent().append("})()");
	    }
	}

	private void transpileIdsToDelete(Transpiler transpiler) {
	    if(this.deletables==null)
	        transpiler.append("null");
	    else {
	        transpiler.append("(function() { ").indent();
	        transpiler.append("var idsToDelete = new Set();").newLine();
	        this.deletables.forEach(exp -> {
	            exp.transpile(transpiler);
	            transpiler.append(".collectDbIds(idsToDelete);").newLine();
	        });
	        transpiler.append("return idsToDelete;").newLine();
	        transpiler.dedent().append("})()");
	    }
	}

	
}
