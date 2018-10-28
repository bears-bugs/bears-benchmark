package prompto.declaration;

import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.grammar.NativeCategoryBindingList;
import prompto.javascript.JavaScriptNativeCategoryBinding;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class NativeWidgetDeclaration extends NativeCategoryDeclaration implements IWidgetDeclaration {
	
	public NativeWidgetDeclaration(Identifier id, NativeCategoryBindingList categoryBindings, MethodDeclarationList methods) {
		super(id, null, categoryBindings, null, methods);
	}
	
	@Override
	public boolean isAWidget(Context context) {
		return true;
	}

	@Override
	protected void categoryTypeToEDialect(CodeWriter writer) {
		writer.append("native widget");
	}
	
	@Override
	protected void categoryTypeToODialect(CodeWriter writer) {
		writer.append("native widget");
	}
	
	@Override
	protected void categoryTypeToMDialect(CodeWriter writer) {
		writer.append("native widget");
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
		JavaScriptNativeCategoryBinding binding = this.getJavaScriptBinding();
	    binding.transpileWidget(transpiler, getName());
	    return true;
	}


	
}
