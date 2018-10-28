package prompto.intrinsic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import prompto.error.ReadWriteError;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/** a data structure which ties a piece of binary data to a mime type 
 * This is a bridge between BinaryValue and any IStore implementation
 */
public class PromptoBinary {

	private String mimeType;
	private byte[] bytes;
	
	public PromptoBinary() {
	}
	
	public PromptoBinary(String mimeType, byte[] bytes) {
		this.mimeType = mimeType;
		this.bytes = bytes;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	@SuppressWarnings("unchecked")
	public void populateFrom(Object value) {
		if(value instanceof PromptoDocument)
			populateFromDocument((PromptoDocument<String,Object>)value);
		else
			throw new UnsupportedOperationException();
	}

	private void populateFromDocument(PromptoDocument<String, Object> value) {
		try {
			Map<String, byte[]> datas = collectData(value);
			bytes = zipData(datas);
			mimeType = "application/zip";
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}

	private Map<String, byte[]> collectData(PromptoDocument<String, Object> value) throws IOException {
		Map<String, byte[]> binaries = new HashMap<>();
		// create textual data
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createGenerator(output);
		value.toJson(generator, null, null, true, binaries);
		generator.flush();
		generator.close();
		// add it
		binaries.put("value.json", output.toByteArray());
		return binaries;
	}

	private byte[] zipData(Map<String, byte[]> datas) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(output);
		for(Map.Entry<String, byte[]> part : datas.entrySet()) {
			ZipEntry entry = new ZipEntry(part.getKey());
			zip.putNextEntry(entry);
			zip.write(part.getValue());
			zip.closeEntry();
		}
		zip.close();
		return output.toByteArray();
	}

	
}
