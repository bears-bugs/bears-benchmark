package prompto.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlConfig.WriteClassName;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.esotericsoftware.yamlbeans.document.YamlDocument;
import com.esotericsoftware.yamlbeans.document.YamlDocumentReader;

public class TestYamlUtils {

	@Test
	public void testCopy() throws IOException {
		File targetFile = File.createTempFile("sample-", ".yml");
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			try(Reader reader = new InputStreamReader(input)) {
				YamlDocument yaml = new YamlDocumentReader(reader).read();
				try(Writer writer = new FileWriter(targetFile)) {
					YamlConfig config = new YamlConfig();
					config.writeConfig.setWriteClassname(WriteClassName.NEVER);
					config.writeConfig.setAutoAnchor(false);
					YamlWriter target = new YamlWriter(writer, config);
					target.write(yaml);
					writer.close();
				} 
			}
		}
		checkYamlEntries(targetFile);
	}

	@SuppressWarnings("unchecked")
	private void checkYamlEntries(File file) throws IOException {
		YamlReader reader = new YamlReader(new FileReader(file));
		Map<String, Object> data = (Map<String, Object>) reader.read();
		assertEquals("true", data.get("boolean"));
		assertEquals("some string", data.get("string"));
		assertEquals("23456", data.get("integer"));
		assertEquals(Arrays.asList("abc", "def"), data.get("array"));
		Map<String, Object> child = (Map<String, Object>) data.get("codeStore");
		assertEquals("stuff", child.get("stuff"));
		List<Object> list = (List<Object>) data.get("objectsArray");
		assertEquals(2, list.size());
		child = (Map<String, Object>) list.get(0);
		assertEquals("abc", child.get("key"));
		assertEquals("def", child.get("value"));
		child = (Map<String, Object>) data.get("simple");
		assertEquals("some string", child.get("string"));
		assertEquals("23456", child.get("integer"));
		assertEquals(Arrays.asList("abc", "def"), child.get("array"));
		assertNull(child.get("more"));
		child = (Map<String, Object>) data.get("imported");
		assertEquals("some string", child.get("string"));
		assertEquals("23456", child.get("integer"));
		assertEquals(Arrays.asList("abc", "def"), child.get("array"));
		assertEquals("more", child.get("more"));
	}
}
/*
boolean: true
string: some string
integer: 23456
array: 
  - abc
  - def
objectsArray:
  - key: abc
    value: def
  - key: ghi
    value: klm
      
simple: &simple
  string: some string
  integer: 23456
  array: 
    - abc
    - def
    
imported: 
  <<: *simple   
  more: more
  
codeStore:
  stuff: stuff
*/