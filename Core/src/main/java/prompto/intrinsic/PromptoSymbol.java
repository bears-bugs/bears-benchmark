package prompto.intrinsic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* the root class for enumerated native symbols */
public abstract class PromptoSymbol implements PromptoEnum {

	public static List<PromptoSymbol> getSymbols(Class<?> klass) {
		List<PromptoSymbol> list = new ArrayList<>();
		for(Field field : klass.getDeclaredFields()) {
			if(Character.isUpperCase(field.getName().charAt(0))) {
				PromptoSymbol symbol = new PromptoSymbol() {
					@Override
					public String getName() {
						return field.getName();
					}
					@Override
					public Object getValue() {
						try {
							return field.get(null);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							return null;
						}
					}
				};
				list.add(symbol);
			}
		}
		return list;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
