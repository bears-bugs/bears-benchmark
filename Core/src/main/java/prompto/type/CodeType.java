package prompto.type;

import java.lang.reflect.Type;

import prompto.runtime.Context;
import prompto.store.Family;



public class CodeType extends NativeType {

	static CodeType instance = new CodeType();
	
	public static CodeType instance() {
		return instance;
	}
	
	private CodeType() {
		super(Family.CODE);
	}
	
	@Override
	public Type getJavaType(Context context) {
		return null;
	}
}
