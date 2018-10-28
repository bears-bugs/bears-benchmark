package prompto.store.memory;

import java.util.Collection;
import java.util.Map;

import prompto.store.AttributeInfo;
import prompto.store.IQueryBuilder.MatchOp;

public class MatchesPredicate<T extends Object> implements IPredicate {

	AttributeInfo info;
	MatchOp match;
	T value;
	
	public MatchesPredicate(AttributeInfo info, MatchOp match, T value) {
		this.info = info;
		this.match = match;
		this.value = value;
	}
	
	@Override
	public boolean matches(Map<String, Object> document) {
		Object data = document.get(info.getName());
		switch(match) {
		case EQUALS:
			return matchesEQUALS(data);
		case ROUGHLY:
			return matchesROUGHLY(data);
		case CONTAINS:
			return matchesCONTAINS(data);
		case HAS:
			return matchesHAS(data);
		case IN:
			return matchesIN(data);
		case GREATER:
			return matchesGREATER(data);
		case LESSER:
			return matchesLESSER(data);
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private boolean matchesGREATER(Object data) {
		if(data instanceof Comparable && value instanceof Comparable)
			return ((Comparable<Object>)data).compareTo((Comparable<Object>)value)>0;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	private boolean matchesLESSER(Object data) {
		if(data instanceof Comparable && value instanceof Comparable)
			return ((Comparable<Object>)data).compareTo((Comparable<Object>)value)<0;
		else
			return false;
	}

	private boolean matchesHAS(Object data) {
		if(data instanceof Collection)
			return ((Collection<?>)data).contains(value);
		else
			return false;
	}

	private boolean matchesCONTAINS(Object data) {
		if(data instanceof String && value instanceof String)
			return ((String)data).contains((String)value);
		else if(data instanceof Collection)
			return ((Collection<?>)data).stream().anyMatch(this::matchesCONTAINS);
		else
			return false;
	}

	private boolean matchesIN(Object data) {
		if(data instanceof String && value instanceof String)
			return ((String)value).contains((String)data);
		else if(value instanceof Collection)
			return ((Collection<?>)value).stream().anyMatch(item->item.equals(data));
		else
			return false;
	}

	private boolean matchesROUGHLY(Object data) {
		if(data instanceof String && value instanceof String)
			return ((String)data).equalsIgnoreCase((String)value);
		else
			return matchesEQUALS(data);
	}

	private boolean matchesEQUALS(Object data) {
		if(data==null) 
			return value==null;
		else
			return data.equals(value);
	}

}
