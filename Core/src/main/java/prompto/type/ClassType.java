package prompto.type;

import java.lang.reflect.Type;

import prompto.runtime.Context;
import prompto.store.Family;

public class ClassType extends BaseType {

	static ClassType instance = new ClassType();
	
	public static ClassType instance() {
		return instance;
	}
	
	private ClassType() {
		super(Family.TYPE);
	}

	@Override
	public Type getJavaType(Context context) {
		return Class.class;
	}

	@Override
	public void checkUnique(Context context) {
	}

	@Override
	public void checkExists(Context context) {
	}

	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return false;
	}

	@Override
	public boolean isMoreSpecificThan(Context context, IType other) {
		return false;
	}

}
