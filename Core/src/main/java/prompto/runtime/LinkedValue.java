package prompto.runtime;

import prompto.type.IType;
import prompto.value.BaseValue;


/* used to ensure downcast local resolves to actual value */
public class LinkedValue extends BaseValue {
	
	Context context;

	public LinkedValue(Context context, IType type) {
		super(type);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
	
}
