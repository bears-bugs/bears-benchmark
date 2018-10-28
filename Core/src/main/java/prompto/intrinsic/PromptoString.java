package prompto.intrinsic;

import java.util.Collection;

public abstract class PromptoString {

	public static String multiply(String s, int count) {
		char[] src = s.toCharArray();
		char[] cc = new char[count * src.length];
		for (int i = 0; i < count; i++)
			System.arraycopy(src, 0, cc, i * src.length, src.length);
		return new String(cc);
	}
	
	public static boolean contains(String s, Object o) {
		if(o instanceof java.lang.String)
			return s.contains((java.lang.String)o);
		else if(o instanceof java.lang.Character)
			return s.indexOf(((java.lang.Character)o).charValue())>=0;
		else
			throw new IllegalArgumentException(o.getClass().getName());
	}

	public static boolean containsAll(String s, Object o) {
		if(o instanceof java.lang.String)
			return containsAll(s, (java.lang.String)o);
		else if(o instanceof Collection)
			return containsAll(s, (Collection<?>)o);
		else
			throw new IllegalArgumentException(o.getClass().getName());
	}

	private static boolean containsAll(String s, String o) {
		for(char c : o.toCharArray()) {
			if(s.indexOf(c)<0)
				return false;
		}
		return true;
	}

	private static boolean containsAll(String s, Collection<?> items) {
		for(Object item : items) {
			if(!contains(s, item))
				return false;
		}
		return true;
	}

	public static boolean containsAny(String s, Object o) {
		if(o instanceof java.lang.String)
			return containsAny(s, (java.lang.String)o);
		else if(o instanceof Collection)
			return containsAny(s, (Collection<?>)o);
		else
			throw new IllegalArgumentException(o.getClass().getName());
	}
	
	private static boolean containsAny(String s, String o) {
		for(char c : o.toCharArray()) {
			if(s.indexOf(c)>=0)
				return true;
		}
		return false;
	}

	private static boolean containsAny(String s, Collection<?> items) {
		for(Object item : items) {
			if(contains(s, item))
				return true;
		}
		return false;
	}
	
	public static String convertObjectToExact(Object o) {
		if(o==null)
			return null;
		else
			return o.toString();
	}


}
