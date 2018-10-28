package prompto.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;


public class TestYamlConfigurationReader {

	@Test
	public void testThatYamlFileIsParsed() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			Map<String, Object> data = YamlConfigurationReader.parseYaml(input);
			assertNotNull(data);
			assertNotNull(data.get("simple"));
		}
	}
	

	@Test
	public void testThatReaderReadsStrings() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			assertEquals("some string", reader.getString("string"));
		}
	}
	
	@Test
	public void testThatReaderReadsNull() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			assertNull(reader.getString("empty"));
		}
	}
	
	@Test
	public void testThatReaderReadsBoolean() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			assertTrue(reader.getBoolean("boolean"));
		}
	}

	@Test
	public void testThatReaderReadsInteger() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			assertEquals(23456, reader.getInteger("integer").intValue());
		}
	}
	
	@Test
	public void testThatReaderReadsArray() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			assertEquals(Arrays.asList("abc", "def"), reader.getArray("array"));
		}
	}
	
	@Test
	public void testThatReaderReadsObject() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			IConfigurationReader child = reader.getObject("simple");
			assertEquals("some string", child.getString("string"));
			assertEquals(23456, child.getInteger("integer").intValue());
			assertEquals(Arrays.asList("abc", "def"), child.getArray("array"));
		}
	}
	
	@Test
	public void testThatReaderReadsObjectsArray() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			Collection<? extends IConfigurationReader> list = reader.getObjectsArray("objectsArray");
			assertEquals(2, list.size());
			IConfigurationReader item = list.iterator().next();
			assertEquals("abc", item.getString("key"));
			assertEquals("def", item.getString("value"));
		}
	}

	@Test
	public void testThatReaderReadsImported() throws IOException  {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.yml")) {
			IConfigurationReader reader = new YamlConfigurationReader(input);
			IConfigurationReader child = reader.getObject("imported");
			assertEquals("some string", child.getString("string"));
			assertEquals("more", child.getString("more"));
		}
	}

}
