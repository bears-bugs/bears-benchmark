package prompto.reader;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.junit.Test;

import prompto.intrinsic.PromptoDocument;

public class TestCSVReader {

	@Test
	public void testNullRetursnEmptyIterator() throws IOException {
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator((String)null, null, ',', '"');
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testEmptyRetursnEmptyIterator() throws IOException {
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator("", null, ',', '"');
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testSimpleNoQuotes() throws IOException {
		String csv = "id,name\n1,John\n2,Sylvie\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertEquals("John", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Sylvie", doc.get("name"));
	}

	@Test
	public void testEscapeNoQuotes() throws IOException {
		String csv = "id,name\n1,John\n2,Riou\\, Sylvie\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertEquals("John", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Riou, Sylvie", doc.get("name"));
	}

	@Test
	public void testSimpleQuotes() throws IOException {
		String csv = "\"id\",\"name\"\n1,\"John\"\n2,\"Sylvie\"\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertEquals("John", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Sylvie", doc.get("name"));
	}

	@Test
	public void testEmptyValue() throws IOException {
		String csv = "\"id\",\"name\"\n,\"John\"\n2,\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertNull(doc.get("id"));
		assertEquals("John", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertNull(doc.get("name"));
	}

	@Test
	public void testMissingValue() throws IOException {
		String csv = "\"id\",\"name\"\n1\n2,\"Sylvie\"\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertNull(doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Sylvie", doc.get("name"));
	}

	@Test
	public void testExtraValue() throws IOException {
		String csv = "\"id\",\"name\"\n1,\"John\",Doe\n2,\"Sylvie\"\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertEquals("John", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Sylvie", doc.get("name"));
	}
	
	@Test
	public void testInnerQuote() throws Exception {
		String csv = "id,name\n1,Jo\"hn\n2,Sylvie\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertEquals("Jo\"hn", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Sylvie", doc.get("name"));
	}
	
	@Test
	public void testQuotedInnerQuote() throws Exception {
		String csv = "id,name\n1,\"Jo\"\"hn\"\n2,Sylvie\n";
		Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(csv, null, ',', '"');
		PromptoDocument<String, Object> doc = iter.next();
		assertNotNull(doc);
		assertEquals("1", doc.get("id"));
		assertEquals("Jo\"hn", doc.get("name"));
		doc = iter.next();
		assertNotNull(doc);
		assertEquals("2", doc.get("id"));
		assertEquals("Sylvie", doc.get("name"));
	}
	
	@Test
	public void testMarkets() throws Exception {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("markets.csv")) {
			try(InputStreamReader reader = new InputStreamReader(input)) {
				Iterator<PromptoDocument<String, Object>> iter = CSVReader.iterator(reader, null, ';', '"');
				while(iter.hasNext()) {
					PromptoDocument<String, Object> doc = iter.next();
					assertNotNull(doc.get("MIC"));
				}
			}
		}
		
	}
}
