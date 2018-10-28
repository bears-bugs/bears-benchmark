package prompto.csharp;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.statement.NativeCall;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class CSharpNativeCall extends NativeCall {

	CSharpStatement statement;
	
	public CSharpNativeCall(CSharpStatement statement) {
		this.statement = statement;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("C#: ");
		statement.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		return VoidType.instance(); // TODO
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		throw new RuntimeException("Should never get there!");
	}

}
