package prompto.type;

import java.util.HashMap;

import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;



public class TypeMap extends HashMap<Identifier, IType> {

	private static final long serialVersionUID = 1L;

	public IType inferType(Context context) {
		if(size()==0)
			return VoidType.instance();
		IType type = null;
		// first pass: get less specific type
		for(IType t : values()) {
			if(type==null)
				type = t;
			else if(type.isAssignableFrom(context, t))
				continue;
			else if(t.isAssignableFrom(context, type))
				type = t;
			else
				throw new SyntaxError("Incompatible types: " + type.getTypeName() + " and " + t.getTypeName());
		}
		// second pass: check compatible
		for(IType t : values()) {
			if(t!=type && !type.isAssignableFrom(context, t))
				throw new SyntaxError("Incompatible types: " + type.getTypeName() + " and " + t.getTypeName());
		}
		return type;
	}

}
