package prompto.declaration;

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
import prompto.statement.IStatement;
import prompto.statement.StatementList;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public class GetterMethodDeclaration extends ConcreteMethodDeclaration implements IExpression {

	public GetterMethodDeclaration(Identifier id, StatementList statements) {
		super(id, null, null, statements);
	}
	
	public static String getNameAsKey(Identifier id) {
		return "getter:" + id.toString();
	}

	@Override
	public String getNameAsKey() {
		return getNameAsKey(getId());
	}

	@Override
	protected void toODialect(CodeWriter writer) {
		writer.append("getter ");
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
		writer.append(" as getter doing:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}

	@Override
	protected void toMDialect(CodeWriter writer) {
		writer.append("def ");
		writer.append(getName());
		writer.append(" getter():\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}

	@Override
	public void check(ConcreteCategoryDeclaration declaration, Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IType check(Context context) {
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, getId());
		return decl.getType();
	}

	public void compile(Context context, ClassFile classFile, Flags flags, CategoryType type, FieldInfo field) {
		String name = CompilerUtils.getterName(this.getName());
		Descriptor.Method proto = new Descriptor.Method(field.getType());
		MethodInfo method = classFile.newMethod(name, proto);
		method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		context = context.newInstanceContext(type, false).newChildContext();
		for(IStatement stmt : statements)
			stmt.compile(context, method, flags);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.statements.transpile(transpiler);
		return true;
	}


}
