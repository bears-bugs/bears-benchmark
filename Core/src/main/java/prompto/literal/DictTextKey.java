package prompto.literal;

import prompto.utils.StringUtils;

public class DictTextKey extends DictKey {

	String text;
	
	public DictTextKey(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	protected String asKey() {
		return StringUtils.unescape(text);
	}

}
