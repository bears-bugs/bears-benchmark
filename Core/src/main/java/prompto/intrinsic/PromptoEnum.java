package prompto.intrinsic;

import java.lang.reflect.Field;

public interface PromptoEnum {

	public static PromptoEnum getInstance(Object name, Class<?> klass) {
		try {
			Field field = klass.getField((String)name);
			return (PromptoEnum)field.get(null);
		} catch(Throwable t) {
			return null;
		}
	}
	

	String getName();
	Object getValue();
	
}
