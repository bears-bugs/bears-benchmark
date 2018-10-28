package prompto.store.memory;

import prompto.store.AttributeInfo;

public interface IOrderBy {

	AttributeInfo getAttributeInfo();
	boolean isDescending();

}
