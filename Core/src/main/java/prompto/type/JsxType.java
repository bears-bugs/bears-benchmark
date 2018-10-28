package prompto.type;

import java.lang.reflect.Type;

import prompto.runtime.Context;
import prompto.store.Family;

public class JsxType extends NativeType {

	static JsxType instance = new JsxType();
	
	public static JsxType instance() {
		return instance;
	}
	
	private JsxType() {
		super(Family.JSX);
	}

	@Override
	public Type getJavaType(Context context) {
		throw new UnsupportedOperationException("Should never get there!");
	}

}
