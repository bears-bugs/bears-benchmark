package prompto.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;


public class ParserUtils {

	public static Map<Integer, String> extractTokenNames(Class<?> klass) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		for(Field f : klass.getDeclaredFields()) {
			if(f.getType()!=int.class)
				continue;
			if(!f.getName().equals(f.getName().toUpperCase()))
				continue;
			int mask = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;
			if((f.getModifiers() & mask)!=mask)
				continue;
			try {
				int value = f.getInt(null);
				result.put(value, f.getName());
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	public static String getFullText(ParserRuleContext ctx) {
		Token start = ctx.start;
		Token stop = ctx.stop;
		if(start == null || stop == null || start.getStartIndex()<0 || stop.getStopIndex()<0)
			return ctx.getText();
		Interval interval = Interval.of(start.getStartIndex(), stop.getStopIndex());
		return start.getInputStream().getText(interval);
	}
}
