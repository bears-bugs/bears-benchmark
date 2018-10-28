package prompto.grammar;

import prompto.runtime.Context;
import prompto.type.IType;

/* something that can be referred to by name, and returns a type */
public interface INamed {
	Identifier getId();
	default String getName() {
		return getId().toString();
	}
	IType getType(Context context);
}
