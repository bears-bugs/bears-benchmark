package prompto.statement;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import prompto.argument.CodeArgument;
import prompto.argument.IArgument;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.declaration.AbstractMethodDeclaration;
import prompto.declaration.BuiltInMethodDeclaration;
import prompto.declaration.ClosureDeclaration;
import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.DispatchMethodDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeMethodDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.NotMutableError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IAssertion;
import prompto.expression.IExpression;
import prompto.expression.MethodSelector;
import prompto.expression.ThisExpression;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.Identifier;
import prompto.grammar.Specificity;
import prompto.javascript.JavaScriptNativeCall;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.runtime.MethodFinder;
import prompto.transpiler.Transpiler;
import prompto.type.CodeType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Boolean;
import prompto.value.ClosureValue;
import prompto.value.IValue;

public class MethodCall extends SimpleStatement implements IAssertion {

	MethodSelector selector;
	MethodSelector fullSelector;
	ArgumentAssignmentList assignments;
	String variableName;
	DispatchMethodDeclaration dispatcher;
	
	public MethodCall(MethodSelector selector) {
		this.selector = selector;
	}

	public MethodCall(MethodSelector method, ArgumentAssignmentList assignments) {
		this.selector = method;
		this.assignments = assignments;
	}
	
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public MethodSelector getSelector() {
		return selector;
	}

	public ArgumentAssignmentList getAssignments() {
		return assignments;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if (requiresInvoke(writer))
			writer.append("invoke: ");
		selector.toDialect(writer);
		if (assignments != null)
			assignments.toDialect(writer);
		else if (writer.getDialect() != Dialect.E)
			writer.append("()");
	}

	private boolean requiresInvoke(CodeWriter writer) {
		if (writer.getDialect() != Dialect.E || (assignments!=null && !assignments.isEmpty()))
			return false;
		try {
			MethodFinder finder = new MethodFinder(writer.getContext(), this);
			IMethodDeclaration declaration = finder.findBestMethod(false);
			/* if method is a reference */
			return declaration instanceof AbstractMethodDeclaration || declaration.getClosureOf()!=null;
		} catch(SyntaxError e) {
			// not an error
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(selector.toString());
		sb.append('(');
		if(assignments != null && assignments.size() > 0) {
			assignments.forEach((ass)->
				{
					sb.append(ass.toString());
					sb.append(", ");
				});
			sb.setLength(sb.length()-2);
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public IType check(Context context) {
		return check(context, false);
	}
	
	public IType check(Context context, boolean updateSelectorParent) {
		MethodFinder finder = new MethodFinder(context, this);
		IMethodDeclaration declaration = finder.findBestMethod(false);
		if(declaration==null)
			return null;
		if(updateSelectorParent && declaration.getMemberOf()!=null && this.selector.getParent()==null)
			this.selector.setParent(new ThisExpression());
		Context local = isLocalClosure(context) ? context : selector.newLocalCheckContext(context, declaration);
		return check(declaration, context, local);
	}

	private boolean isLocalClosure(Context context) {
		if(this.selector.getParent()!=null)
			return false;
		IDeclaration decl = context.getLocalDeclaration(IDeclaration.class, this.selector.getId());
		return decl instanceof MethodDeclarationMap;
	}

	private IType check(IMethodDeclaration declaration, Context parent, Context local) {
		if (declaration.isTemplate())
			return fullCheck((ConcreteMethodDeclaration) declaration, parent, local);
		else
			return lightCheck(declaration, parent, local);
	}

	private IType lightCheck(IMethodDeclaration declaration, Context parent, Context local) {
		declaration.registerArguments(local);
		return declaration.check(local, false);
	}

	private IType fullCheck(ConcreteMethodDeclaration declaration, Context parent, Context local) {
		try {
			ArgumentAssignmentList assignments = makeAssignments(parent, declaration);
			declaration.registerArguments(local);
			for (ArgumentAssignment assignment : assignments) {
				IExpression expression = assignment.resolve(local, declaration, true, false);
				IValue value = assignment.getArgument().checkValue(parent, expression);
				local.setValue(assignment.getArgumentId(), value);
			}
			return declaration.check(local, false);
		} catch (PromptoError e) {
			throw new SyntaxError(e.getMessage());
		}
	}

	public ArgumentAssignmentList makeAssignments(Context context, IMethodDeclaration declaration) {
		ArgumentAssignmentList assignments = this.assignments;
		if (assignments == null)
			assignments = new ArgumentAssignmentList();
		return assignments.makeAssignments(context, declaration);
	}

	public ArgumentAssignmentList makeCodeAssignments(Context context, IMethodDeclaration declaration) {
		if (assignments == null)
			return new ArgumentAssignmentList();
		else {
			ArgumentAssignmentList list = new ArgumentAssignmentList();
			list.addAll(assignments.stream()
					.filter((a)->
						(a.getExpression().check(context)==CodeType.instance()))
					.collect(Collectors.toList()));
			return list.resolveAndCheck(context, declaration);
		}
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		MethodFinder finder = new MethodFinder(context, this);
		Collection<IMethodDeclaration> declarations = finder.findPotentialMethods();
		switch(declarations.size()) {
		case 0:
			throw new SyntaxError("No matching prototype for:" + this.toString()); 
		case 1:
			return compileExact(context, method, flags, declarations.iterator().next());
		default:
			return compileDynamic(context, method, flags, finder.findLessSpecific(declarations));
		}
	}
	
	private ResultInfo compileDynamic(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration) {
		Context local = this.selector.newLocalCheckContext(context, declaration);
		declaration.registerArguments(local);
		ArgumentAssignmentList assignments = this.assignments!=null ? this.assignments : new ArgumentAssignmentList();
		return this.selector.compileDynamic(local, method, flags, declaration, assignments);
	}

	private ResultInfo compileExact(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration) {
		if(declaration.isTemplate())
			return compileTemplate(context, method, flags, declaration);
		else
			return compileConcrete(context, method, flags, declaration);
	}

	private ResultInfo compileConcrete(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration) {
		Context local = isLocalClosure(context) ? context : selector.newLocalCheckContext(context, declaration);
		declaration.registerArguments(local);
		ArgumentAssignmentList assignments = this.assignments!=null ? this.assignments : new ArgumentAssignmentList();
		return this.selector.compileExact(local, method, flags, declaration, assignments);
	}

	private ResultInfo compileTemplate(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration) {
		// compile the method as a member method
		Context local = context.newLocalContext();
		declaration.registerArguments(local);
		registerCodeAssignments(context, local, declaration);
		String methodName = declaration.compileTemplate(local, false, method.getClassFile());
		// compile the method call
		IExpression parent = method.isStatic() ? null : new ThisExpression();
		MethodSelector selector = new MethodSelector(parent, new Identifier(methodName));
		local = selector.newLocalContext(context, declaration);
		declaration.registerArguments(local);
		registerCodeAssignments(context, local, declaration);
		ArgumentAssignmentList assignments = this.assignments!=null ? this.assignments : new ArgumentAssignmentList();
		return selector.compileTemplate(local, method, flags, declaration, assignments, methodName);
	}

	private void registerCodeAssignments(Context context, Context local, IMethodDeclaration declaration) {
		ArgumentAssignmentList assignments = makeCodeAssignments(context, declaration);
		for (ArgumentAssignment assignment : assignments) {
			IExpression expression = assignment.resolve(local, declaration, true, false);
			IArgument argument = assignment.getArgument();
			IValue value = argument.checkValue(context, expression);
			local.setValue(assignment.getArgumentId(), value);
		}	
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		IMethodDeclaration declaration = findDeclaration(context);
		Context local = selector.newLocalContext(context, declaration);
		local.enterMethod(declaration);
		try {
			declaration.registerArguments(local);
			registerAssignments(context, local, declaration);
			return declaration.interpret(local);
		} finally {
			local.leaveMethod(declaration);
		}
	}

	private void registerAssignments(Context context, Context local, IMethodDeclaration declaration) throws PromptoError {
		ArgumentAssignmentList assignments = makeAssignments(context, declaration);
		for (ArgumentAssignment assignment : assignments) {
			IExpression expression = assignment.resolve(local, declaration, true, false);
			IArgument argument = assignment.getArgument();
			IValue value = argument.checkValue(context, expression);
			if(value!=null && argument.isMutable() & !value.isMutable()) 
				throw new NotMutableError();
			local.setValue(assignment.getArgumentId(), value);
		}
	}

	@Override
	public boolean interpretAssert(Context context, TestMethodDeclaration test) throws PromptoError {
		IValue value = this.interpret(context);
		if(value instanceof Boolean) {
			if(((Boolean)value).getValue())
				return true;
			else {
				String expected = buildExpectedMessage(context, test);
				String actual = value.toString();
				test.printFailedAssertion(context, expected, actual);
				return false;
			}
		} else {
			CodeWriter writer = new CodeWriter(this.getDialect(), context);
			this.toDialect(writer);
			throw new SyntaxError("Cannot test '" + writer.toString() + "'");
		}
	}
	
	private String buildExpectedMessage(Context context, TestMethodDeclaration test) {
		CodeWriter writer = new CodeWriter(test.getDialect(), context);
		this.toDialect(writer);
		return writer.toString();
	}

	@Override
	public void compileAssert(Context context, MethodInfo method, Flags flags, TestMethodDeclaration test) {
		StackState finalState = method.captureStackState();
		// compile
		ResultInfo info = this.compile(context, method, flags.withPrimitive(true));
		if(java.lang.Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		// 1 = success 
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener); 
		// increment failure counter
		method.addInstruction(Opcode.ICONST_1);
		method.addInstruction(Opcode.IADD);
		// build failure message
		String message = buildExpectedMessage(context, test);
		message = test.buildFailedAssertionMessagePrefix(message);
		method.addInstruction(Opcode.LDC, new StringConstant(message));
		method.addInstruction(Opcode.LDC, new StringConstant(Boolean.FALSE.toString()));
		MethodConstant concat = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		test.compileFailure(context, method, flags);
		// success/final
		method.restoreFullStackState(finalState);
		method.placeLabel(finalState);
		method.inhibitOffsetListener(finalListener);
	}
	
	private IMethodDeclaration findDeclaration(Context context) {
		try {
			Object o = context.getValue(selector.getId());
			if (o instanceof ClosureValue)
				return new ClosureDeclaration((ClosureValue)o);
		} catch (PromptoError e) {
		}
		MethodFinder finder = new MethodFinder(context, this);
		return finder.findBestMethod(true);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		Context context = transpiler.getContext();
		MethodFinder finder = new MethodFinder(context, this);
	    Set<IMethodDeclaration> declarations = finder.findCompatibleMethods(false, true, spec -> spec!= Specificity.INCOMPATIBLE);
	    if(declarations.size()==1 && declarations.iterator().next() instanceof BuiltInMethodDeclaration) {
            ((BuiltInMethodDeclaration)declarations.iterator().next()).declareCall(transpiler);
	    } else {
	        if (this.assignments != null)
	            this.assignments.declare(transpiler);
        	if(!this.isLocalClosure(context)) {
		        declarations.forEach(declaration -> {
		            Context local = this.selector.newLocalCheckContext(transpiler.getContext(), declaration);
		            this.declareDeclaration(transpiler, declaration, local);
		        });
        	}
	        if(declarations.size()>1 && this.dispatcher==null) {
	        	IMethodDeclaration declaration = finder.findBestMethod(false);
	        	List<IMethodDeclaration> sorted = finder.sortMostSpecificFirst(declarations);
	            this.dispatcher = new DispatchMethodDeclaration(transpiler.getContext(), this, declaration, sorted);
	            transpiler.declare(this.dispatcher);
	        }
	    }
	}

	private void declareDeclaration(Transpiler transpiler, IMethodDeclaration declaration, Context local) {
	    if(declaration.isTemplate()) {
	        this.fullDeclareDeclaration(declaration, transpiler, local);
	    } else {
	        this.lightDeclareDeclaration(declaration, transpiler, local);
	    }
	}

	static AtomicLong fullDeclareCounter = new AtomicLong();
	
	private void fullDeclareDeclaration(IMethodDeclaration declaration, Transpiler transpiler, Context local) {
	    if(this.fullSelector==null) {
	    	List<ArgumentAssignment> assignments = this.makeAssignments(transpiler.getContext(), declaration);
	        declaration.registerArguments(local);
	        assignments.forEach(assignment -> {
	            IExpression expression = assignment.resolve(local, declaration, true, false);
	            IValue value = assignment.getArgument().checkValue(transpiler.getContext(), expression);
	            local.setValue(assignment.getArgument().getId(), value);
	        });
	        Transpiler localTranspiler = transpiler.copyTranspiler(local);
	        this.fullSelector = this.selector.newFullSelector(fullDeclareCounter.incrementAndGet());
	        declaration.fullDeclare(localTranspiler, this.fullSelector.getId());
	    }
	}

	private void lightDeclareDeclaration(IMethodDeclaration declaration, Transpiler transpiler, Context local) {
	    transpiler = transpiler.copyTranspiler(local);
	    declaration.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		MethodFinder finder = new MethodFinder(transpiler.getContext(), this);
	    Set<IMethodDeclaration> declarations = finder.findCompatibleMethods(false, true, spec -> spec!=Specificity.INCOMPATIBLE);
	    if (declarations.size() == 1)
	        this.transpileSingle(transpiler, declarations.iterator().next(), false);
	    else
	        this.transpileMultiple(transpiler, declarations);
	    return false;
	}

	private void transpileSingle(Transpiler transpiler, IMethodDeclaration declaration, boolean allowDerived) {
	   if (declaration instanceof BuiltInMethodDeclaration)
	        this.transpileBuiltin(transpiler, (BuiltInMethodDeclaration)declaration);
	   else if(declaration.hasAnnotation(transpiler.getContext(), "Inlined"))
		   throw new UnsupportedOperationException("Yet!");
	   else if(declaration.containerHasAnnotation(transpiler.getContext(), "Inlined"))
		   this.transpileInlinedMemberMethod(transpiler, declaration);
	   else {
	        this.transpileSelector(transpiler, declaration);
	        this.transpileAssignments(transpiler, declaration, allowDerived);
	    }
	}

	private void transpileInlinedMemberMethod(Transpiler transpiler, IMethodDeclaration declaration) {
		if(!(declaration instanceof NativeMethodDeclaration))
			throw new UnsupportedOperationException("Can only inline native methods!");
		transpileInlinedMemberMethod(transpiler, (NativeMethodDeclaration)declaration);
	}

	private void transpileInlinedMemberMethod(Transpiler transpiler, NativeMethodDeclaration declaration) {
		JavaScriptNativeCall call = declaration.findCall(JavaScriptNativeCall.class);
		if(call==null)
			throw new UnsupportedOperationException("Missing native JavaScript call!");
		call.transpileInlineMethodCall(transpiler, declaration, this);
	}
	
	
	private void transpileAssignments(Transpiler transpiler, IMethodDeclaration declaration, boolean allowDerived) {
		List<ArgumentAssignment> assignments = this.makeAssignments(transpiler.getContext(), declaration);
	    assignments = assignments.stream().filter(assignment->!(assignment.getArgument() instanceof CodeArgument)).collect(Collectors.toList());
	    if(!assignments.isEmpty()) {
	        transpiler.append("(");
	        assignments.forEach(assignment -> {
	            IArgument argument = assignment.getArgument();
	            IExpression expression = assignment.resolve(transpiler.getContext(), declaration, false, allowDerived);
	            argument.transpileCall(transpiler, expression);
	            transpiler.append(", ");
	        });
	        transpiler.trimLast(2);
	        transpiler.append(")");
	    } else
	        transpiler.append("()");
	}

	public void transpileSelector(Transpiler transpiler, IMethodDeclaration declaration) {
		MethodSelector selector = resolveSelector(transpiler, declaration);
	    selector.transpile(transpiler);
	}
	
	public MethodSelector resolveSelector(Transpiler transpiler, IMethodDeclaration declaration) {
	    MethodSelector selector = /*this.fullSelector ||*/ this.selector;
	    IExpression parent = selector.resolveParent(transpiler.getContext());
	    if (parent == null && declaration.getMemberOf()!=null && transpiler.getContext().getClosestInstanceContext()!=null)
	        parent = new ThisExpression();
	    String name = null;
	    if(this.variableName!=null)
	        name = this.variableName;
	    /*else if(this.fullSelector)
	        name = this.fullSelector.name;*/
	    else 
	        name = declaration.getTranspiledName(transpiler.getContext());
	    return new MethodSelector(parent, new Identifier(name));
	}

	private void transpileBuiltin(Transpiler transpiler, BuiltInMethodDeclaration declaration) {
	    IExpression parent = this.selector.resolveParent(transpiler.getContext());
	    parent.transpile(transpiler);
	    transpiler.append(".");
	    declaration.transpileCall(transpiler, this.assignments);
	}

	private void transpileMultiple(Transpiler transpiler, Set<IMethodDeclaration> declarations) {
	    String name = this.dispatcher.getTranspiledName(transpiler.getContext());
	    IExpression parent = this.selector.resolveParent(transpiler.getContext());
	    if(parent==null && declarations.iterator().next().getMemberOf()!=null && transpiler.getContext().getClosestInstanceContext()!=null)
	        parent = new ThisExpression();
	    MethodSelector selector = new MethodSelector(parent, new Identifier(name));
	    selector.transpile(transpiler);
	    this.transpileAssignments(transpiler, this.dispatcher, false);
	}



}
