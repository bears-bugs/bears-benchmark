package prompto.reader;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import prompto.intrinsic.PromptoDocument;
import prompto.reader.JSONReader;
import prompto.runtime.Context;
import prompto.utils.ResourceUtils;
import prompto.value.Document;
import prompto.value.IValue;

public class TestJSONReader {

	@Test
	public void testRoundtripIntrinsic() throws IOException {
		String json1 = ResourceUtils.getResourceAsString("samples/gitPushSample.json");
		Object obj1 = JSONReader.read(json1);
		String json2 = obj1.toString();
		Object obj2 = JSONReader.read(json2);
		assertEquals(obj1, obj2);
	}

	@Test
	public void testRoundtripIValue() throws IOException {
		String json1 = ResourceUtils.getResourceAsString("samples/gitPushSample.json");
		Object obj1 = JSONReader.read(json1);
		assertTrue(obj1 instanceof PromptoDocument);
		IValue doc1 = new Document(Context.newGlobalContext(), (PromptoDocument<?,?>)obj1);  
		String json2 = doc1.toString();
		Object obj2 = JSONReader.read(json2);
		assertEquals(obj1, obj2);
	}
}
