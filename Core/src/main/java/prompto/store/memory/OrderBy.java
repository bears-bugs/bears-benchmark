package prompto.store.memory;

import prompto.store.AttributeInfo;

public class OrderBy implements IOrderBy {
	
	AttributeInfo attribute;
	boolean descending;
	
	public OrderBy(AttributeInfo attribute, boolean descending) {
		this.attribute = attribute;
		this.descending = descending;
	}
	
	@Override
	public AttributeInfo getAttributeInfo() {
		return attribute;
	}
	
	@Override
	public boolean isDescending() {
		return descending;
	}
	
}
