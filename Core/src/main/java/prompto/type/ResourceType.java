package prompto.type;

import prompto.grammar.Identifier;
import prompto.store.Family;

public class ResourceType extends CategoryType {

	public ResourceType(Identifier name) {
		super(Family.RESOURCE, name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof ResourceType))
			return false;
		ResourceType other = (ResourceType)obj;
		return this.getTypeNameId().equals(other.getTypeNameId());
	}
		
}
