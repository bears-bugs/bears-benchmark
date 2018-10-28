package prompto.statement;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.ExceptionHandler;
import prompto.compiler.FieldConstant;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.error.ExecutionError;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.expression.SymbolExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoException;
import prompto.literal.ListLiteral;
import prompto.runtime.Context;
import prompto.runtime.ErrorVariable;
import prompto.transpiler.Transpiler;
import prompto.type.EnumeratedCategoryType;
import prompto.type.IType;
import prompto.type.TypeMap;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class SwitchErrorStatement extends BaseSwitchStatement {

	Identifier errorId;
	StatementList statements;
	StatementList finallyStatements;
	
	public SwitchErrorStatement(Identifier errorName, StatementList statements) {
		this.errorId = errorName;
		this.statements = statements;
	}
	
	public SwitchErrorStatement(Identifier errorName, StatementList statements, 
			SwitchCaseList handlers, StatementList anyStmts, StatementList finalStmts) {
		super(handlers, anyStmts);
		this.errorId = errorName;
		this.statements = statements;
		this.finallyStatements = finalStmts;
	}

	public void setAlwaysInstructions(StatementList list) {
		finallyStatements = list;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer = writer.newLocalWriter();
		writer.getContext().registerValue(new ErrorVariable(errorId));
		super.toDialect(writer);
	}
	
	@Override
	protected void toODialect(CodeWriter writer) {
		writer.append("try (");
		writer.append(errorId);
		writer.append(") {\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("} ");
		for(SwitchCase sc : switchCases)
			sc.catchToODialect(writer);
		if(defaultCase!=null) {
			writer.append("catch(any) {\n");
			writer.indent();
			defaultCase.toDialect(writer);
			writer.dedent();
			writer.append("}");
		}
		if(finallyStatements!=null) {
			writer.append("finally {\n");
			writer.indent();
			finallyStatements.toDialect(writer);
			writer.dedent();
			writer.append("}");
		}
		writer.newLine();
	}

	@Override
	protected void toMDialect(CodeWriter writer) {
		writer.append("try ");
		writer.append(errorId);
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		for(SwitchCase sc : switchCases)
			sc.catchToPDialect(writer);
		if(defaultCase!=null) {
			writer.append("except:\n");
			writer.indent();
			defaultCase.toDialect(writer);
			writer.dedent();
		}
		if(finallyStatements!=null) {
			writer.append("finally:\n");
			writer.indent();
			finallyStatements.toDialect(writer);
			writer.dedent();
		}
		writer.newLine();
	}
	@Override
	protected void toEDialect(CodeWriter writer) {
		writer.append("switch on ");
		writer.append(errorId);
		writer.append(" doing:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		for(SwitchCase sc : switchCases)
			sc.catchToEDialect(writer);
		if(defaultCase!=null) {
			writer.append("when any:\n");
			writer.indent();
			defaultCase.toDialect(writer);
			writer.dedent();
		}
		if(finallyStatements!=null) {
			writer.append("always:\n");
			writer.indent();
			finallyStatements.toDialect(writer);
			writer.dedent();
		}
	}

	@Override
	protected void checkSwitchCasesType(Context context) {
		Context local = context.newLocalContext();
		local.registerValue(new ErrorVariable(errorId));
		super.checkSwitchCasesType(local);
	}
	
	@Override
	IType checkSwitchType(Context context) {
		return new EnumeratedCategoryType(new Identifier("Error"));
	}
	
	@Override
	protected void collectReturnTypes(Context context, TypeMap types) {
		IType type = statements.check(context, null);
		if(type!=VoidType.instance())
			types.put(type.getTypeNameId(), type);
		Context local = context.newLocalContext();
		local.registerValue(new ErrorVariable(errorId));
		super.collectReturnTypes(local, types);
		if(finallyStatements!=null) {
			type = finallyStatements.check(context, null);
			if(type!=VoidType.instance())
				types.put(type.getTypeNameId(), type);
		}
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue result = null;
		try {
			result = statements.interpret(context);
		} catch (ExecutionError e) {
			IValue switchValue = e.interpret(context, errorId);
			result = interpretSwitch(context, switchValue, e);
		} finally {
			if(finallyStatements!=null)
				finallyStatements.interpret(context);
		}
		return result;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		List<List<ExceptionHandler>> handlers = installExceptionHandlers(context, method, flags);
		ResultInfo result = statements.compile(context, method, flags);
		if(result.isReturn() || result.isThrow()) 
			compileExceptionHandlers(context, method, flags, handlers, null);
		else {
			List<OffsetListenerConstant> finalOffsets = new LinkedList<>();
			StackState neutral = method.captureStackState();
			OffsetListenerConstant finalOffset = method.addOffsetListener(new OffsetListenerConstant());
			method.activateOffsetListener(finalOffset);
			finalOffsets.add(finalOffset);
			method.addInstruction(Opcode.GOTO, finalOffset);
			compileExceptionHandlers(context, method, flags, handlers, finalOffsets);
			finalOffsets.forEach((o)->
				method.inhibitOffsetListener(o));
			method.restoreFullStackState(neutral);
			method.placeLabel(neutral);
		}
		return result;
	}

	private void compileExceptionHandlers(Context context, MethodInfo method,
			Flags flags, List<List<ExceptionHandler>> handlerList, List<OffsetListenerConstant> finalOffsets ) {
		Iterator<SwitchCase> iterCases = switchCases.iterator();
		Iterator<List<ExceptionHandler>> iterHandler = handlerList.iterator();
		if(iterCases.hasNext())
			compileExceptionHandler(context, method, flags, iterCases.next(), iterHandler.next(), finalOffsets);
		if(defaultCase!=null)
			compileExceptionHandler(context, method, flags, null, iterHandler.next(), finalOffsets);
		// TODO 'finally'
	}

	private void compileExceptionHandler(Context context, MethodInfo method, Flags flags, 
			SwitchCase switchCase, List<ExceptionHandler> handlers,
			List<OffsetListenerConstant> finalOffsets) {
		handlers.forEach((h)->
			method.inhibitOffsetListener(h));
		ExceptionHandler handler = makeCommonExceptionHandler(handlers);
		method.placeExceptionHandler(handler);
		Type exception = compileConvertException(context, method, flags, handler);
		StackLocal error = method.registerLocal(errorId.toString(), 
				VerifierType.ITEM_Object, new ClassConstant(exception));
		CompilerUtils.compileASTORE(method, error);
		Context local = context.newLocalContext();
		local.registerValue(new ErrorVariable(errorId));
		ResultInfo result = switchCase!=null ? 
				switchCase.statements.compile(local, method, flags) :
				defaultCase.compile(context, method, flags);
		if(finalOffsets!=null && !result.isReturn() && !result.isThrow()) {
			OffsetListenerConstant finalOffset = method.addOffsetListener(new OffsetListenerConstant());
			method.activateOffsetListener(finalOffset);
			finalOffsets.add(finalOffset);
			method.addInstruction(Opcode.GOTO, finalOffset);
		}
		method.unregisterLocal(error);
	}

	private Type compileConvertException(Context context, MethodInfo method, Flags flags, String name) {
		method.addInstruction(Opcode.POP); // the original exception
		Type classType = CompilerUtils.getCategoryEnumConcreteType("Error");
		ClassConstant cc = new ClassConstant(classType);
		Type fieldType = CompilerUtils.getExceptionType(classType, name);
		FieldConstant fc = new FieldConstant(cc, name, fieldType);
		method.addInstruction(Opcode.GETSTATIC, fc);
		return cc.getType();
	}

	private ExceptionHandler makeCommonExceptionHandler(List<ExceptionHandler> handlers) {
		if(handlers.size()==1) 
			return handlers.get(0);
		else
			throw new UnsupportedOperationException();
	}

	private List<List<ExceptionHandler>> installExceptionHandlers(Context context, 
			MethodInfo method, Flags flags) {
		List<List<ExceptionHandler>> handlers = new LinkedList<>();
		for(SwitchCase sc : switchCases)
			handlers.add(installExceptionHandlers(context, method, flags, sc));
		if(defaultCase!=null)
			handlers.add(installDefaultExceptionHandlers(context, method, flags));
			
		// TODO 'finally'
		return handlers;
	}

	private List<ExceptionHandler> installDefaultExceptionHandlers(Context context, MethodInfo method, Flags flags) {
		List<ExceptionHandler> list = new LinkedList<ExceptionHandler>();
		list.add(installExceptionHandler(context, method, flags, (SymbolExpression)null));
		return list;
	}

	private List<ExceptionHandler> installExceptionHandlers(Context context, MethodInfo method,
			Flags flags,  SwitchCase sc) {
		if(sc instanceof AtomicSwitchCase)
			return installExceptionHandler(context, method, flags, (AtomicSwitchCase)sc);
		else if(sc instanceof CollectionSwitchCase)
			return installExceptionHandlers(context, method, flags, (CollectionSwitchCase)sc);
		else
			throw new UnsupportedOperationException();
	}

	private List<ExceptionHandler> installExceptionHandler(Context context, MethodInfo method,
			Flags flags, AtomicSwitchCase sc) {
		IExpression exp = sc.getExpression();
		if(exp instanceof SymbolExpression) {
			List<ExceptionHandler> list = new LinkedList<ExceptionHandler>();
			list.add(installExceptionHandler(context, method, flags, (SymbolExpression)exp));
			return list;
		} else
			throw new UnsupportedOperationException();
		
	}

	private List<ExceptionHandler> installExceptionHandlers(Context context, MethodInfo method,
			Flags flags, CollectionSwitchCase sc) {
		IExpression exp = sc.getExpression();
		if(exp instanceof ListLiteral) {
			List<ExceptionHandler> list = new LinkedList<ExceptionHandler>();
			for(IExpression item : ((ListLiteral)exp).getExpressions()) {
				if(item instanceof SymbolExpression)
					list.add(installExceptionHandler(context, method, flags, (SymbolExpression)item));
				else
					throw new UnsupportedOperationException();
			}
			return list;
		} else
			throw new UnsupportedOperationException();

	}

	private Type compileConvertException(Context context, MethodInfo method, Flags flags, ExceptionHandler handler) {
		Type type = handler.getException().getType();
		if(type instanceof Class) {
			String simpleName = PromptoException.getExceptionTypeName((Class<?>)type);
			if(type.getTypeName().endsWith(simpleName))
				return type;
			else
				return compileConvertException(context, method, flags, simpleName);
		} else
			return type;
	}

	private ExceptionHandler installExceptionHandler(Context context, MethodInfo method,
			Flags flags, SymbolExpression symbol) {
		Type type = getExceptionType(context, symbol);
		ExceptionHandler handler = method.registerExceptionHandler(type);
		method.activateOffsetListener(handler);
		return handler;
	}

	private Type getExceptionType(Context context, SymbolExpression symbol) {
		if(symbol==null)
			return CompilerUtils.getCategoryEnumConcreteType("Error");
		else {
			Type type = PromptoException.getExceptionType(symbol.getName());
			if(type!=null)
				return type;
			else
				return symbol.getJavaType(context);
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("NativeError");
	    this.statements.declare(transpiler);
	    transpiler = transpiler.newLocalTranspiler();
	    transpiler.getContext().registerValue(new ErrorVariable(this.errorId));
	    this.declareSwitch(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("try {").indent();
	    this.statements.transpile(transpiler);
	    transpiler.dedent().append("} catch(").append(this.errorId.toString()).append(") {").indent();
	    Transpiler child = transpiler.newLocalTranspiler();
	    child.getContext().registerValue(new ErrorVariable(this.errorId));
	    child.append("switch(translateError(").append(this.errorId.toString()).append(")) {").indent();
	    this.switchCases.forEach(switchCase -> {
	        switchCase.transpileError(child);
	    });
	    if(this.defaultCase!=null) {
	    	child.append("default:").indent();
	        this.defaultCase.transpile(child);
	        child.dedent();
	    }
	    child.dedent().append("}");
	    if(this.finallyStatements!=null) {
	    	child.append(" finally {").indent();
	        this.finallyStatements.transpile(child);
	        child.dedent().append("}");
	    }
	    child.dedent().append("}");
	    child.flush();
	    return true;
	}
}
