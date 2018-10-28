package prompto.argument;

import java.lang.reflect.Type;
import java.util.Objects;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.IDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.store.DataStore;
import prompto.store.IStore;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.utils.TypeUtils;
import prompto.value.IValue;

public class AttributeArgument extends BaseArgument implements INamedArgument {
	
	public AttributeArgument(Identifier id) {
		super(id);
	}

	@Override
	public String getSignature(Dialect dialect) {
		return id.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(id);
		if(defaultExpression!=null) {
			writer.append(" = ");
			defaultExpression.toDialect(writer);
		}
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	@Override
	public String getProto() {
		return id.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof AttributeArgument))
			return false;
		AttributeArgument other = (AttributeArgument)obj;
		return Objects.equals(this.getId(),other.getId());
	}

	@Override
	public void register(Context context) {
		context.registerValue(this, true);
		if(defaultExpression!=null) try {
			context.setValue(id, defaultExpression.interpret(context));
		} catch(PromptoError error) {
			throw new SyntaxError("Unable to register default value: "+ defaultExpression.toString() + " for argument: " + id);
		}
	}
	
	@Override
	public void check(Context context) {
		AttributeDeclaration actual = context.getRegisteredDeclaration(AttributeDeclaration.class,id);
		if(actual==null)
			throw new SyntaxError("Unknown attribute: \"" + id + "\"");
	}
	
	@Override
	public IType getType(Context context) {
		// dbId type can only be resolved at runtime
		if(IStore.dbIdName.equals(id.toString()))
			return TypeUtils.typeToIType(DataStore.getInstance().getDbIdClass());
		else {
			IDeclaration named = context.getRegisteredDeclaration(IDeclaration.class, id);
			return named.getType(context);
		}
	}
	
	@Override
	public IValue checkValue(Context context, IExpression expression) throws PromptoError {
		AttributeDeclaration actual = context.getRegisteredDeclaration(AttributeDeclaration.class, id);
		return actual.checkValue(context, expression);
	}
	
	@Override
	public Type getJavaType(Context context) {
		return CompilerUtils.getAttributeInterfaceType(id);
	}
	
	@Override
	public StackLocal registerLocal(Context context, MethodInfo method, Flags flags) {
		String desc = CompilerUtils.getDescriptor(getJavaType(context));
		VerifierType type = VerifierType.fromDescriptor(desc);
		ClassConstant classConstant = new ClassConstant(getJavaType(context));
		String instanceName = "%" + getName() + "%";
		return method.registerLocal( instanceName, type, classConstant);
	}
	
	@Override
	public void extractLocal(Context context, MethodInfo method, Flags flags) {
		super.registerLocal(context, method, flags);
		String instanceName = "%" + getName() + "%";
		CompilerUtils.compileALOAD(method, instanceName);
		Type klass = getJavaType(context);
		String getterName = CompilerUtils.getterName(getName());
		AttributeDeclaration actual = context.getRegisteredDeclaration(AttributeDeclaration.class, id);
		FieldInfo field = actual.toFieldInfo(context);
		InterfaceConstant m = new InterfaceConstant(klass, getterName, field.getType());
		method.addInstruction(Opcode.INVOKEINTERFACE, m);
		StackLocal local = method.getRegisteredLocal(getName());
		CompilerUtils.compileASTORE(method, local);
	}
	
	@Override
	public void compileAssignment(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments, boolean isFirst) {
		ArgumentAssignment assign = makeAssignment(assignments, isFirst);
		IType itype = assign.getExpression().check(context.getCallingContext());
		// if param is a category, assume it implements the required attribute interface
		if(itype instanceof CategoryType)
			assign.getExpression().compile(context.getCallingContext(), method, flags);
		// if param is a value, wrap it into an attribute wrapper
		else {
			Type type = CompilerUtils.getAttributeConcreteType(id);
			CompilerUtils.compileNewRawInstance(method, type);
			method.addInstruction(Opcode.DUP);
			ResultInfo info = assign.getExpression().compile(context.getCallingContext(), method, flags);
			CompilerUtils.compileCallConstructor(method, type, info.getType());
		}
	}
	
	@Override
	public String getTranspiledName(Context context) {
		return this.id.toString();
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    IDeclaration decl = transpiler.getContext().getRegisteredDeclaration(IDeclaration.class, this.id);
	    decl.declare(transpiler);
	}
	
	@Override
	public void transpileCall(Transpiler transpiler, IExpression expression) {
	    AttributeDeclaration decl = transpiler.getContext().getRegisteredDeclaration(AttributeDeclaration.class, this.id);
	    if(decl.getConstraint()!=null) {
	        transpiler.append("$check_").append(this.getName()).append("(");
	        super.transpileCall(transpiler, expression);
	        transpiler.append(")");
	    } else
	        super.transpileCall(transpiler, expression);
	}
}
