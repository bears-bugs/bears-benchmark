package prompto.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.document.YamlMapping;

public class SecretKeyConfiguration implements ISecretKeyConfiguration {

	protected IConfigurationReader reader;

	public SecretKeyConfiguration(IConfigurationReader reader) {
		this.reader = reader;
	}

	@Override
	public String getFactory() {
		return reader.getString("factory");
	}

	@Override
	public char[] getSecret() {
		String value = reader.getString("secret");
		return value==null ? null : value.toCharArray();
	}
	
	@Override
	public YamlMapping toYaml() throws YamlException {
		YamlMapping yaml = new YamlMapping();
		yaml.setEntry("factory", reader.getString("factory"));
		yaml.setEntry("secret", reader.getString("secret"));
		return yaml;
	}
	
	
	
}
