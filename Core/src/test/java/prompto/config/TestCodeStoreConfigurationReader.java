package prompto.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import prompto.store.IStorable;
import prompto.store.IStore;
import prompto.store.memory.MemStore;


public class TestCodeStoreConfigurationReader {

	IStore store;
	Object dbId;
	
	@Before
	public void before() {
		store = new MemStore();
		IStorable doc = store.newStorable(Collections.singletonList("config"), (dbId)->this.dbId=dbId);
		doc.setData("string", "some string");
		doc.setData("empty", null);
		doc.setData("boolean", true);
		doc.setData("integer", 23456);
		doc.setData("array", Arrays.asList("abc", "def"));
		// child record ref
		IStorable child = store.newStorable(Collections.singletonList("child"), (dbId)->doc.setData("ref", dbId));
		child.setData("string", "some string");
		child.setData("integer", 23456);
		child.setData("array", Arrays.asList("abc", "def"));
		store.store(child);	
		// array record refs
		List<Object> children = new ArrayList<>();
		child = store.newStorable(Collections.singletonList("child"), (dbId)->children.add(dbId));
		child.setData("key", "abc");
		child.setData("value", "def");
		store.store(child);	
		child = store.newStorable(Collections.singletonList("child"), (dbId)->children.add(dbId));
		child.setData("key", "abcd");
		child.setData("value", "defg");
		store.store(child);	
		doc.setData("objectsArray", children);
		store.store(doc);
	}
	
	
	@Test
	public void testThatReaderReadsStrings() throws Exception  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		assertEquals("some string", reader.getString("string"));
	}
	
	
	@Test
	public void testThatReaderReadsNull() throws IOException  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		assertNull(reader.getString("empty"));
	}
	
	@Test
	public void testThatReaderReadsBoolean() throws IOException  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		assertTrue(reader.getBoolean("boolean"));
	}

	
	@Test
	public void testThatReaderReadsInteger() throws IOException  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		assertEquals(23456, reader.getInteger("integer").intValue());
	}

	
	@Test
	public void testThatReaderReadsArray() throws IOException  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		assertEquals(Arrays.asList("abc", "def"), reader.getArray("array"));
	}

	
	@Test
	public void testThatReaderReadsRefObject() throws IOException  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		IConfigurationReader child = reader.getObject("ref");
		assertEquals("some string", child.getString("string"));
		assertEquals(23456, child.getInteger("integer").intValue());
		assertEquals(Arrays.asList("abc", "def"), child.getArray("array"));
	}
	
	@Test
	public void testThatReaderReadsObjectsArray() throws IOException  {
		IConfigurationReader reader = new StoredRecordConfigurationReader(store, dbId);
		Collection<? extends IConfigurationReader> list = reader.getObjectsArray("objectsArray");
		assertEquals(2, list.size());
		IConfigurationReader item = list.iterator().next();
		assertEquals("abc", item.getString("key"));
		assertEquals("def", item.getString("value"));
	}
}
