package prompto.store;

import java.util.Collection;

public class AttributeInfo {

	public static final AttributeInfo CATEGORY = new AttributeInfo("category", Family.TEXT, true, null);
	public static final AttributeInfo NAME = new AttributeInfo("name", Family.TEXT, false, null);
	public static final AttributeInfo STORABLE = new AttributeInfo("storable", Family.BOOLEAN, false, null);
	public static final AttributeInfo SYMBOLS = new AttributeInfo("symbols", Family.TEXT, true, null);
	
	public static final String KEY = "key";
	public static final String VALUE = "value";
	public static final String WORDS = "words";

	protected String name;
	protected Family family;
	protected boolean collection;
	protected boolean key = false;
	protected boolean value = false;
	protected boolean words = false;
	
	public AttributeInfo(String name, Family family, boolean collection, Collection<String> indexTypes) {
		this.name = name;
		this.family = family;
		this.collection = collection;
		if(indexTypes!=null) {
			key = indexTypes.contains(KEY);
			value = indexTypes.contains(VALUE);
			words = indexTypes.contains(WORDS);
		} 
	}
	
	public AttributeInfo(String name, Family family, boolean collection, boolean key, boolean value, boolean words) {
		this.name = name;
		this.family = family;
		this.collection = collection;
		this.key = key;
		this.value = value;
		this.words = words;
	}

	public AttributeInfo(AttributeInfo info) {
		this.name = info.getName();
		this.family = info.getFamily();
		this.collection = info.isCollection();
		this.key = info.isKey();
		this.value = info.isValue();
		this.words = info.isWords();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public Family getFamily() {
		return family;
	}

	public boolean isCollection() {
		return collection;
	}
	
	public boolean isKey() {
		return key;
	}
	
	public boolean isValue() {
		return value;
	}
	
	public boolean isWords() {
		return words;
	}

	public String toTranspiled() {
	    return "new AttributeInfo('" + this.name + "', TypeFamily." + this.family.name() + ", " + this.collection + ", null)";
	}
}
