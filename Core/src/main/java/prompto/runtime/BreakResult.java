package prompto.runtime;

import prompto.type.VoidType;
import prompto.value.BaseValue;

public class BreakResult extends BaseValue {
	
	protected BreakResult() {
		super(VoidType.instance());
	}

	private static BreakResult instance = new BreakResult();
	
	public static BreakResult instance() {
		return instance;
	}

}
