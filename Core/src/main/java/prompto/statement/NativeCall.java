package prompto.statement;

import prompto.transpiler.Transpiler;


public abstract class NativeCall extends SimpleStatement {
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		return true; // skipped
	}
}
