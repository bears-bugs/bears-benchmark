package prompto.security;

import prompto.config.ISecretKeyConfiguration;

public class PlainSecretKeyFactory implements ISecretKeyFactory {

	ISecretKeyConfiguration config;
	
	public PlainSecretKeyFactory() {
	}
	
	public PlainSecretKeyFactory(ISecretKeyConfiguration config) {
		this.config = config;
	}

	@Override
	public String getAsPlainText() {
		char[] value = config.getSecret();
		return value==null ? null : new String(value);
	}

}
