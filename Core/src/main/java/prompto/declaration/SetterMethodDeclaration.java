package prompto.declaration;

import prompto.argument.AttributeArgument;
import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodInfo;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.statement.IStatement;
import prompto.statement.StatementList;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public class SetterMethodDeclaration extends ConcreteMethodDeclaration implements IExpression {

	public SetterMethodDeclaration(Identifier id, StatementList statements) {
		super(id, null, null, statements);
	}

	public static String getNameAsKey(Identifier id) {
		return "Setter:" + id.toString();
	}

	@Override
	public String getNameAsKey() {
		return getNameAsKey(getId());
	}

	@Override
	protected void toODialect(CodeWriter writer) {
		writer.append("setter ");
		writer.append(getName());
		writer.append(" {\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("}\n");
	}

	@Override
	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(getName());
		writer.append(" as setter doing:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}	

	@Override
	protected void toMDialect(CodeWriter writer) {
		writer.append("def ");
		writer.append(getName());
		writer.append(" setter():\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}	

	@Override
	public void check(ConcreteCategoryDeclaration category, Context context) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public IType check(Context context) {
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, getId());
		return decl.getType();
	}

	@Override
	public void compile(Context context, boolean isStart, ClassFile classFile) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void compile(Context context, ClassFile classFile, Flags flags,
			CategoryType type, FieldInfo field) {
		String name = CompilerUtils.setterName(this.getName());
		Descriptor.Method proto = new Descriptor.Method(field.getType(), void.class);
		MethodInfo method = classFile.newMethod(name, proto);
		method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, getId());
		method.registerLocal(getName(), VerifierType.ITEM_Object, new ClassConstant(field.getType()));
		context = context.newInstanceContext(type, false).newChildContext();
		context.registerValue(new Variable(getId(), decl.getType()));
		for(IStatement stmt : statements)
			stmt.compile(context, method, flags.withMember(true).withInline(true).withSetter(field));
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    AttributeArgument arg = new AttributeArgument(this.getId());
	    arg.register(transpiler.getContext());
	    this.statements.transpile(transpiler);
	    return false;
	}

}
