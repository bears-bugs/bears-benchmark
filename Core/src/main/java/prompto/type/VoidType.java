package prompto.type;

import java.lang.reflect.Type;

import prompto.runtime.Context;
import prompto.store.Family;


public class VoidType extends NativeType {

	static VoidType instance = new VoidType();
	
	public static VoidType instance() {
		return instance;
	}
	
	private VoidType() {
		super(Family.VOID);
	}
	
	@Override
	public Type getJavaType(Context context) {
		return void.class;
	}

	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		throw new RuntimeException("Should never get there !");
	}
	
}
