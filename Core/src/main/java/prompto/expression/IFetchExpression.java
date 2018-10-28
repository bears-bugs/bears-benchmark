package prompto.expression;

import prompto.value.IValue;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.store.DataStore;
import prompto.store.IStore;

public interface IFetchExpression extends IExpression {
	
	IValue fetch(Context context, IStore store);
	Object fetchRaw(IStore store);
	
	default IValue interpret(Context context) throws PromptoError {
		return fetch(context, DataStore.getInstance());
	}

}
