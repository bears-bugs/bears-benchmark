package prompto.type;

import java.lang.reflect.Type;

import prompto.runtime.Context;
import prompto.store.Family;

public class CssType extends NativeType {

	static CssType instance = new CssType();
	
	public static CssType instance() {
		return instance;
	}
	
	private CssType() {
		super(Family.CSS);
	}

	@Override
	public Type getJavaType(Context context) {
		throw new UnsupportedOperationException("Should never get there!");
	}

}
