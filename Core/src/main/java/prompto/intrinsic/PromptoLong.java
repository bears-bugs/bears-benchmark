package prompto.intrinsic;


public abstract class PromptoLong {

	public static Long convertObjectToExact(Object o) {
		if(o==null)
			return null;
		else if(o instanceof Long)
			return (Long)o;
		else if(o instanceof Double)
			return ((Double)o).longValue();
		else if(o instanceof String)
			return Long.decode((String)o);
		else
			throw new ClassCastException("Cannot convert from " 
						+ o.getClass().getSimpleName()
						+ " to Long");
	}

}