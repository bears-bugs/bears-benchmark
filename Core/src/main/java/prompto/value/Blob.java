package prompto.value;

import prompto.intrinsic.PromptoBinary;
import prompto.type.BlobType;

public class Blob extends BinaryValue {

	public Blob() {
		super(BlobType.instance());
	}
	
	
	public Blob(PromptoBinary data) {
		super(BlobType.instance(), data);
	}


	public Blob(String mimeType, byte[] bytes) {
		this(new PromptoBinary(mimeType, bytes));
	}

}
