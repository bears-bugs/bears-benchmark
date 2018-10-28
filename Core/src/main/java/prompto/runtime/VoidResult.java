package prompto.runtime;

import prompto.type.VoidType;
import prompto.value.BaseValue;

public class VoidResult extends BaseValue {
	
	protected VoidResult() {
		super(VoidType.instance());
	}

	private static VoidResult instance = new VoidResult();
	
	public static VoidResult instance() {
		return instance;
	}

}
