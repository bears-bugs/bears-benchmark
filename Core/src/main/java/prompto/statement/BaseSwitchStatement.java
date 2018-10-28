package prompto.statement;

import java.util.LinkedList;

import prompto.error.ExecutionError;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.TypeMap;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;


public abstract class BaseSwitchStatement extends BaseStatement {

	public static class SwitchCaseList extends LinkedList<SwitchCase> {

		private static final long serialVersionUID = 1L;
		
		public SwitchCaseList() {
		}

		public SwitchCaseList(SwitchCase item) {
			this.add(item);
		}

	}
	
	SwitchCaseList switchCases;
	StatementList defaultCase;
	
	public BaseSwitchStatement() {
		this.switchCases = new SwitchCaseList();
		this.defaultCase = null;
	}

	public BaseSwitchStatement(SwitchCaseList switchCases, StatementList defaultCase) {
		this.switchCases = switchCases!=null ? switchCases : new SwitchCaseList();
		this.defaultCase = defaultCase;
	}

	public void addSwitchCase(SwitchCase switchCase) {
		switchCases.add(switchCase);
	}

	public void setDefaultCase(StatementList defaultCase) {
		this.defaultCase = defaultCase;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}
	
	protected abstract void toEDialect(CodeWriter writer);
	protected abstract void toODialect(CodeWriter writer);
	protected abstract void toMDialect(CodeWriter writer);

	@Override
	public IType check(Context context) {
		checkSwitchCasesType(context);
		return checkReturnType(context);
	}
	
	@Override
	public boolean canReturn() {
		return true;
	}
	
	protected void checkSwitchCasesType(Context context) {
		IType type = checkSwitchType(context);
		for(SwitchCase sc : switchCases)
			sc.checkSwitchType(context,type);
	}

	abstract IType checkSwitchType(Context context);

	private IType checkReturnType(Context context) {
		TypeMap types = new TypeMap();
		collectReturnTypes(context, types);
		return types.inferType(context);
	}
	
	protected void collectReturnTypes(Context context, TypeMap types) {
		for(SwitchCase sc : switchCases) {
			IType type = sc.checkReturnType(context);
			if(type!=VoidType.instance())
				types.put(type.getTypeNameId(), type);
		}
		if(defaultCase!=null) {
			IType type = defaultCase.check(context, null);
			if(type!=VoidType.instance())
				types.put(type.getTypeNameId(), type);
		}
	}

	protected IValue interpretSwitch(Context context, IValue switchValue, ExecutionError toThrow) throws PromptoError {
		for(SwitchCase sc : switchCases) {
			if(sc.matches(context, switchValue))
				return sc.interpret(context);
		}
		if(defaultCase!=null)
			return defaultCase.interpret(context);
		if(toThrow!=null)
			throw toThrow;
		return null;
	}

	protected void declareSwitch(Transpiler transpiler) {
	    this.switchCases.forEach( kase -> {
	        kase.declare(transpiler);
	    });
	    if(this.defaultCase!=null) {
	        this.defaultCase.declare(transpiler);
	    }
	}

}
