package prompto.security;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import prompto.config.ISecretKeyConfiguration;

public class TestSecretKeyFactory {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

	@Test
	public void testThatPlainSecretKeyFactoryReturnsPlainPassword() throws Throwable {
		ISecretKeyConfiguration config = new ISecretKeyConfiguration() {

			@Override public String getFactory() { return PlainSecretKeyFactory.class.getName(); }
			@Override public char[] getSecret() { return "password".toCharArray(); }
			
		};
		assertEquals("password", ISecretKeyFactory.plainPasswordFromConfig(config));
	}
	
	@Test
	public void testThatEnvironmentVariableSecretKeyFactoryReturnsPlainPassword() throws Throwable {
		environmentVariables.set("some key", "password");
		ISecretKeyConfiguration config = new ISecretKeyConfiguration() {

			@Override public String getFactory() { return EnvironmentVariableSecretKeyFactory.class.getName(); }
			@Override public char[] getSecret() { return "some key".toCharArray(); }
			
		};
		assertEquals("password", ISecretKeyFactory.plainPasswordFromConfig(config));
	}

}
