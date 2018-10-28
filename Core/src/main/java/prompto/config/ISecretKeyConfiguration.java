package prompto.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.document.YamlMapping;


public interface ISecretKeyConfiguration {

	String getFactory();
	char[] getSecret();
	default YamlMapping toYaml() throws YamlException {
		throw new RuntimeException();
	}
	
}
