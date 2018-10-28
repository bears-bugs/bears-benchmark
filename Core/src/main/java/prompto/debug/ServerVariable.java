package prompto.debug;

import prompto.grammar.INamed;
import prompto.runtime.Context;

public class ServerVariable implements IVariable {

	Context context;
	INamed named;
	IValue value;
	
	public ServerVariable(Context context, INamed named) {
		this.context = context;
		this.named = named;
	}
	
	@Override
	public String getName() {
		return named.getName();
	}
	
	@Override
	public String getTypeName() {
		return named.getType(context).getTypeName();
	}
	
	@Override
	public IValue getValue() {
		if(value==null)
			value = new ServerValue(context.getValue(named.getId()));
		return value;
	}
	
}
