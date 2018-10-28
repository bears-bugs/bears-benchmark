package prompto.declaration;

import prompto.compiler.ClassFile;
import prompto.compiler.CompilerException;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.java.JavaNativeCall;
import prompto.runtime.Context;
import prompto.statement.IStatement;
import prompto.statement.NativeCall;
import prompto.statement.StatementList;
import prompto.type.IType;
import prompto.type.TypeMap;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class NativeMethodDeclaration extends ConcreteMethodDeclaration {

	JavaNativeCall statement;
	
	public NativeMethodDeclaration(Identifier name, ArgumentList arguments, IType returnType, StatementList instructions) {
		super(name, arguments, returnType, instructions);
		statement = findCall(JavaNativeCall.class);
	}

	@SuppressWarnings("unchecked")
	public <T extends NativeCall> T findCall(Class<T> klass) {
		for(IStatement statement : statements) {
			if(klass.isAssignableFrom(statement.getClass()))
				return (T)statement;
		}
		return null;
	}

	@Override
	protected IType checkStatements(Context context) {
		if(statement!=null && context.getProblemListener().isCheckNative())
			return checkNative(context);
		else if(returnType!=null)
			return returnType;
		else
			return VoidType.instance();
	}
	
	private IType checkNative(Context context) {
		if(returnType==VoidType.instance()) {
			// don't check return type
			IType type = ((JavaNativeCall)statement).checkNative(context, returnType);
			// TODO: remove the below workaround for unregistered native categories
			if(type==null)
				type = returnType;
			if(type!=VoidType.instance())
				context.getProblemListener().reportIllegalReturn(statement);
			return returnType;
		} else {
			TypeMap types = new TypeMap();
			if(returnType!=null)
				types.put(returnType.getTypeNameId(), returnType);
			// TODO: ensure returnType is registered prior to the below 
			IType type = statement.checkNative(context, returnType);
			// TODO: remove the below workaround for unregistered native categories
			if(type==null)
				type = returnType;
			if(type!=VoidType.instance())
				types.put(type.getTypeNameId(), type);
			type = types.inferType(context);
			if(returnType!=null)
				return returnType;
			else
				return type;
		}
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		try {
			return doInterpretNative(context);
		} catch(NullPointerException e) {
			e.printStackTrace();
			throw new NullReferenceError();
		}
	}
	
	private IValue doInterpretNative(Context context) throws PromptoError {
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
	public void compile(Context context, boolean isStart, ClassFile classFile) {
		compileGlobal(context, classFile);
	}
	
	
	public void compileGlobal(Context context, ClassFile classFile) {
		try {
			context = context.newLocalContext();
			registerArguments(context);
			IType returnType = this.checkNative(context);
			MethodInfo method = createMethodInfo(context, classFile, returnType, getName());
			registerLocals(context, classFile, method);
			if(statement!=null)
				statement.compile(context, method, new Flags());
			// ensure we always return
			if(returnType==VoidType.instance())
				method.addInstruction(Opcode.RETURN);
		} catch (PromptoError e) {
			throw new CompilerException(e);
		}
	}

	
	public ResultInfo compileMember(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments) {
		try {
			// push arguments on the stack
			compileAssignments(context, method, flags, assignments);
			return statement.compile(context, method, new Flags().withMember(true).withInline(true));
		} catch (PromptoError e) {
			throw new CompilerException(e);
		}
	}

	
	@Override
	protected void toMDialect(CodeWriter writer) {
		writer.append("def ");
		if(memberOf==null)
			writer.append("native ");
		writer.append(getName());
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

	@Override
	protected void toODialect(CodeWriter writer) {
		if(returnType!=null  && returnType!=VoidType.instance()) {
			returnType.toDialect(writer);
			writer.append(" ");
		}
		if(memberOf==null)
			writer.append("native ");
		writer.append("method ");
		writer.append(getName());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(") {\n");
		writer.indent();
		for(IStatement statement : statements) {
			statement.toDialect(writer);
			writer.newLine();
		}
		writer.dedent();
		writer.append("}\n");
	}
	
	@Override
	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(getName());
		writer.append(" as ");
		if(memberOf==null)
			writer.append("native ");
		writer.append("method ");
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
		writer.append("\n");
	}

	
}
