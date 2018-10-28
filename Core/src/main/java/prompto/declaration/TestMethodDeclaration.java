package prompto.declaration;

import java.io.PrintStream;
import java.lang.reflect.Modifier;

import prompto.compiler.ClassFile;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.ExceptionHandler;
import prompto.compiler.FieldConstant;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.error.ExecutionError;
import prompto.error.PromptoError;
import prompto.expression.SymbolExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoException;
import prompto.parser.Assertion;
import prompto.runtime.Context;
import prompto.statement.IStatement;
import prompto.statement.StatementList;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.AssertionList;
import prompto.utils.CodeWriter;
import prompto.value.IInstance;
import prompto.value.IValue;

public class TestMethodDeclaration extends BaseDeclaration {

	StatementList statements;
	AssertionList assertions;
	SymbolExpression error;
	
	public TestMethodDeclaration(Identifier name, StatementList stmts, AssertionList exps, SymbolExpression error) {
		super(name);
		this.statements = stmts;
		this.assertions = exps;
		this.error = error;
	}
	
	@Override
	public DeclarationType getDeclarationType() {
		return DeclarationType.TEST;
	}
	
	public StatementList getStatements() {
		return statements;
	}
	
	public AssertionList getAssertions() {
		return assertions;
	}
	
	@Override
	public IType check(Context context, boolean isStart) {
		context = context.newLocalContext();
		for(IStatement statement : statements)
			checkStatement(context, statement);
		if(assertions!=null) {
			for(Assertion assertion : assertions)
				context = assertion.check(context);
		}
		return VoidType.instance();
	}
	
	private void checkStatement(Context context, IStatement statement) {
		IType type = statement.check(context);
		if(type!=null && type!=VoidType.instance()) // null indicates SyntaxError
			context.getProblemListener().reportIllegalReturn(statement);
	}

	@Override
	public void register(Context context) {
		context.registerDeclaration(this);
	}
	
	@Override
	public IType getType(Context context) {
		return VoidType.instance();
	}

	public void interpret(Context context) throws PromptoError {
		if(interpretBody(context)) {
			interpretError(context);
			interpretAsserts(context);
		}
	}

	private void interpretError(Context context) {
		// we land here only if no error was raised
		if(error!=null)
			printFailedAssertion(context, error.getName().toString(), "no error");
	}

	private void interpretAsserts(Context context) throws PromptoError {
		if(assertions==null)
			return;
		context.enterMethod(this);
		try {
			boolean success = true;
			for(Assertion assertion : assertions)
				success &= assertion.interpret(context, this);
			if(success)
				printSuccess(context);
		} finally {
			context.leaveMethod(this);
		}
	}

	public void printFailedAssertion(Context context, String expected, String actual) {
		String message = buildFailedAssertionMessagePrefix(expected);
		System.out.println(message + actual); // TODO use collector but NOT logger
	}

	public String buildFailedAssertionMessagePrefix(String expected) {
		return getName() + " test failed while verifying: " + expected + ", found: ";
	}
	
	public void printMissingError(Context context, String expected, String actual) {
		String message = buildMissingErrorMessagePrefix(expected);
		System.out.println(message + actual); // TODO use collector but NOT logger
	}
	
	public String buildMissingErrorMessagePrefix(String expected) {
		return getName() + " test failed while expecting: " + expected + ", found: ";
	}

	private void printSuccess(Context context) {
		System.out.println(buildSuccessMessage()); // TODO use collector but NOT logger
	}

	public String buildSuccessMessage() {
		return getName() + " test successful";
	}

	private boolean interpretBody(Context context) throws PromptoError {
		context.enterMethod(this);
		try {
			statements.interpret(context);
			return true;
		} catch(ExecutionError e) {
			interpretError(context, e);
			// no more to execute
			return false;
		} finally {
			context.leaveMethod(this);
		}
	}

	private void interpretError(Context context, ExecutionError e) throws PromptoError {
		IValue actual = e.interpret(context, new Identifier("__test_error__"));
		IValue expectedError = error==null ? null : error.interpret(context);
		if(expectedError!=null && expectedError.equals(actual))
			printSuccess(context);
		else {
			String actualName = ((IInstance)actual).getMember(context, new Identifier("name"), false).toString();
			String expectedName = error==null ? "SUCCESS" : error.getName().toString();
			printMissingError(context, expectedName, actualName);
		}
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(writer.isGlobalContext())
			writer = writer.newLocalWriter();
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
	
	protected void toMDialect(CodeWriter writer) {
		writer.append("def test ");
		writer.append(getName());
		writer.append(" ():\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("verifying:");
		if(error!=null) {
			writer.append(" ");
			error.toDialect(writer);
			writer.append("\n");
		} else {
			writer.append("\n");
			writer.indent();
			assertions.toDialect(writer);
			writer.dedent();
		}
	}

	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(getName());
		writer.append(" as test method doing:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("and verifying");
		if(error!=null) {
			writer.append(" ");
			error.toDialect(writer);
			writer.append("\n");
		} else {
			writer.append(":\n");
			writer.indent();
			assertions.toDialect(writer);
			writer.dedent();
		}
	}
	
	protected void toODialect(CodeWriter writer) {
		writer.append("test method ");
		writer.append(getName());
		writer.append(" () {\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("} verifying ");
		if(error!=null) {
			error.toDialect(writer);
			writer.append(";\n");
		} else {
			writer.append("{\n");
			writer.indent();
			assertions.toDialect(writer);
			writer.dedent();
			writer.append("}\n");
		}
	}

	public ClassFile compile(Context context, String fullName) {
		context = context.newLocalContext();
		java.lang.reflect.Type type = CompilerUtils.abstractTypeFrom(fullName);
		ClassFile classFile = new ClassFile(type);
		classFile.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(void.class);
		MethodInfo method = classFile.newMethod("run", proto);
		method.addModifier(Modifier.STATIC);
		if(error!=null)
			compileTestWithError(context, method, new Flags());
		else
			compileTestWithAsserts(context, method, new Flags());
		return classFile;
	}

	private void compileTestWithAsserts(Context context, MethodInfo method, Flags flags) {
		// don't use statements.compile because we need the locals for the assertions
		statements.forEach((s)->
			s.compile(context, method, flags));
		method.addInstruction(Opcode.ICONST_0); // failures counter
		assertions.forEach((a)->
			a.compile(context, method, flags, this));
		compileCheckSuccess(context, method, flags);
		method.addInstruction(Opcode.RETURN);
	}

	private void compileCheckSuccess(Context context, MethodInfo method, Flags flags) {
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener); // 0 = no failures
		StackState finalState = method.captureStackState();
		compileSuccess(context, method, flags);
		// final
		method.restoreFullStackState(finalState);
		method.placeLabel(finalState);
		method.inhibitOffsetListener(finalListener);
		
	}

	public void compileSuccess(Context context, MethodInfo method, Flags flags) {
		String message = buildSuccessMessage();
		method.addInstruction(Opcode.LDC, new StringConstant(message));
		compilePrintResult(context, method, flags);
	}

	public void compileFailure(Context context, MethodInfo method, Flags flags) {
		compilePrintResult(context, method, flags);
	}
	
	public void compilePrintResult(Context context, MethodInfo method, Flags flags) {
		// the message is on top of the stack
		FieldConstant fc = new FieldConstant(System.class, "out", PrintStream.class);
		method.addInstruction(Opcode.GETSTATIC, fc);
		method.addInstruction(Opcode.SWAP);
		MethodConstant mc = new MethodConstant(PrintStream.class, "println", String.class, void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, mc);
	}

	private void compileTestWithError(Context context, MethodInfo method, Flags flags) {
		ExceptionHandler expected = installExpectedExceptionHandler(context, method, flags);
		ExceptionHandler unexpected = installUnexpectedExceptionHandler(context, method, flags);
		statements.compile(context, method, flags);
		// missing exception
		compileMissingExceptionHandler(context, method, flags);
		method.addInstruction(Opcode.RETURN);
		// expected exception
		compileExpectedExceptionHandler(context, method, flags, expected);
		method.addInstruction(Opcode.RETURN);
		// unexpected exception
		compileUnexpectedExceptionHandler(context, method, flags, unexpected);
		method.addInstruction(Opcode.RETURN);
	}

	private void compileUnexpectedExceptionHandler(Context context, MethodInfo method, Flags flags, ExceptionHandler handler) {
		method.placeExceptionHandler(handler); 
		// get actual exception type name
		MethodConstant mc = new MethodConstant(PromptoException.class, "getExceptionTypeName", Object.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, mc); 
		// produce failure message
		String message = buildMissingErrorMessagePrefix(error.getName().toString());
		method.addInstruction(Opcode.LDC, new StringConstant(message)); 
		method.addInstruction(Opcode.SWAP);  
		mc = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, mc); 
		// done
		compilePrintResult(context, method, flags);
	}

	private void compileExpectedExceptionHandler(Context context, MethodInfo method, Flags flags, ExceptionHandler handler) {
		method.placeExceptionHandler(handler);
		method.addInstruction(Opcode.POP); // the thrown exception
		compileSuccess(context, method, flags);
	}

	private void compileMissingExceptionHandler(Context context, MethodInfo method, Flags flags) {
		// produce failure
		String message = buildMissingErrorMessagePrefix(error.getName().toString()) + "no error";
		method.addInstruction(Opcode.LDC, new StringConstant(message));
		compilePrintResult(context, method, flags);
	}

	private ExceptionHandler installUnexpectedExceptionHandler(Context context, MethodInfo method, Flags flags) {
		ExceptionHandler handler = method.registerExceptionHandler(Throwable.class);
		method.activateOffsetListener(handler);
		return handler;
	}

	private ExceptionHandler installExpectedExceptionHandler(Context context, MethodInfo method, Flags flags) {
		java.lang.reflect.Type type = null;
		switch(error.getName()) {
		case "DIVIDE_BY_ZERO":
			type = ArithmeticException.class;
			break;
		case "INDEX_OUT_OF_RANGE":
			type = IndexOutOfBoundsException.class;
			break;
		case "NULL_REFERENCE":
			type = NullPointerException.class;
			break;
		default:
			type = error.getJavaType(context);
		}
		ExceptionHandler handler = method.registerExceptionHandler(type);
		method.activateOffsetListener(handler);
		return handler;
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("NativeError");
	    transpiler.declare(this);
	    transpiler = transpiler.newLocalTranspiler();
	    this.statements.declare(transpiler);
	    if(this.assertions!=null)
	        this.assertions.declare(transpiler);
	    if(this.error!=null)
	        this.error.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler = transpiler.newLocalTranspiler();
	    if (this.error!=null)
	        this.transpileExpectedError(transpiler);
	    else
	        this.transpileAssertions(transpiler);
	    transpiler.flush();
		return true;
	}

	private void transpileAssertions(Transpiler transpiler) {
	    transpiler.append("function ").append(this.getTranspiledName()).append("() {");
	    transpiler.indent();
	    transpiler.append("try {");
	    transpiler.indent();
	    statements.transpile(transpiler);
	    transpiler.append("var success = true;").newLine();
	    assertions.forEach(assertion -> {
	        transpiler.append("if(");
	        assertion.transpile(transpiler);
	        transpiler.append(")").indent();
	        transpiler.append("success &= true;").dedent();
	        transpiler.append("else {").indent();
	        transpiler.append("success = false;").newLine();
	        transpiler.printTestName(this.getName()).append("failed while verifying: ")
	            .append(assertion.getExpected(transpiler.getContext(), this.getDialect()))
	            .append(", found: \" + ");
	        assertion.transpileFound(transpiler, this.getDialect());
	        transpiler.append(");");
	        transpiler.dedent();
	        transpiler.append("}").newLine();
	    });
	    transpiler.append("if (success)").indent().printTestName(this.getName()).append("successful\");").dedent();
	    transpiler.dedent();
	    transpiler.append("} catch (e) {");
	    transpiler.indent();
	    transpiler.printTestName(this.getName()).append("failed with error: \" + e.name);");
	    transpiler.dedent();
	    transpiler.append("}");
	    transpiler.dedent();
	    transpiler.append("}");
	    transpiler.newLine();
	    transpiler.flush();
	}

	public String getTranspiledName() {
		String name = this.getName();
		return name.substring(1, name.length()-1).replaceAll("\\W","_");
	}

	private void transpileExpectedError(Transpiler transpiler) {
	    transpiler.append("function ").append(this.getTranspiledName()).append("() {");
	    transpiler.indent();
	    transpiler.append("try {");
	    transpiler.indent();
	    this.statements.transpile(transpiler);
	    transpiler.printTestName(this.getName()).append("failed while expecting: ").append(this.error.getName()).append(", found: no error\");");
	    transpiler.dedent();
	    transpiler.append("} catch (e) {");
	    transpiler.indent();
	    transpiler.append("if(e instanceof NativeErrors.").append(this.error.getName()).append(") {").indent();
	    transpiler.printTestName(this.getName()).append("successful\");").dedent();
	    transpiler.append("} else {").indent();
	    transpiler.printTestName(this.getName()).append("failed while expecting: ").append(this.error.getName()).append(", found: \" + translateError(e));").dedent();
	    transpiler.append("}");
	    transpiler.dedent();
	    transpiler.append("}");
	    transpiler.dedent();
	    transpiler.append("}");
	    transpiler.newLine();
	    transpiler.flush();
	}

}
