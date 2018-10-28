package prompto.type;

import java.lang.reflect.Type;

import prompto.compiler.CompilerException;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.error.SyntaxError;
import prompto.runtime.Context;

public class NativeCategoryType extends CategoryType {

	NativeCategoryDeclaration decl;
	
	public NativeCategoryType(NativeCategoryDeclaration decl) {
		super(decl.getId());
		this.decl = decl;
	}

	@Override
	public Type getJavaType(Context context) {
		try {
			return decl.getBoundClass(true);
		} catch (SyntaxError e) {
			throw new CompilerException(e);
		}
	}
	
}
