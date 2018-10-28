package prompto.utils;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class StringUtils {

	public static String capitalizeFirst(String value) {
		return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
	}

	public static String capitalizeAll(String value) {
		String[] parts = value.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String part : parts) {
			sb.append(capitalizeFirst(part));
			sb.append(' ');
		}
		if(sb.length()>0)
			sb.setLength(sb.length()-1); // trim last ' '
		return sb.toString();
	}

	public static Character[] stringToCharacterArray(String value) {
		char[] chars = value.toCharArray();
		List<Character> list = new ArrayList<Character>(chars.length);
		for(int i=0;i<chars.length;i++)
			list.add(chars[i]);
		return list.toArray(new Character[chars.length]);
	}
	
	public static String replaceOne(String value, String toReplace, String replaceWith) {
		int idx = value.indexOf(toReplace);
		if(idx<0)
			return value;
		else
			return value.substring(0, idx) + replaceWith + value.substring(idx + toReplace.length());
	}

	public static String escape(String text) {
		return text.replace("\"", "\\\"");
	}

	public static String unescape(String text) {
		StreamTokenizer parser = new StreamTokenizer(new StringReader(text));
		try {
		  parser.nextToken();
		  return parser.sval;
		}
		catch (IOException e) {
		  throw new RuntimeException(e);
		}
	}


}
