package prompto.declaration;

import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.grammar.Identifier;
import prompto.java.JavaNativeCall;
import prompto.runtime.Context;
import prompto.statement.IStatement;
import prompto.statement.StatementList;
import prompto.value.IValue;

public class NativeGetterMethodDeclaration extends GetterMethodDeclaration {

	JavaNativeCall statement;

	public NativeGetterMethodDeclaration(Identifier id, StatementList statements) {
		super(id, statements);
		statement = findNativeStatement();
	}

	private JavaNativeCall findNativeStatement() {
		for(IStatement statement : statements) {
			if(statement instanceof JavaNativeCall)
				return (JavaNativeCall)statement;
		}
		return null;
	}

	@Override
	public IValue interpret(Context context) {
		return doInterpretNative(context);
	}
	
	private IValue doInterpretNative(Context context) {
		context.enterStatement(statement);
		try {
			IValue result = statement.interpretNative(context, returnType);
			if(result!=null)
				return result;
		} finally {
			context.leaveStatement(statement);
		}
		return null;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		if(statement!=null) {
			AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, getId());
			FieldInfo field = decl.toFieldInfo(context);
			return statement.compile(context, method, flags.withInline(true).withMember(true).withGetter(field));
		} else {
			method.addInstruction(Opcode.ACONST_NULL);
			return new ResultInfo(Object.class);
		}
	}
}
