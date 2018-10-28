package prompto.security;

import prompto.config.ISecretKeyConfiguration;

public class EnvironmentVariableSecretKeyFactory implements ISecretKeyFactory {

	ISecretKeyConfiguration config;
	
	public EnvironmentVariableSecretKeyFactory() {
	}
	
	public EnvironmentVariableSecretKeyFactory(ISecretKeyConfiguration config) {
		this.config = config;
	}

	@Override
	public String getAsPlainText() {
		char[] value = config.getSecret();
		return value==null ? null : System.getenv(new String(value));
	}
}
