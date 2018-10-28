package prompto.value;

import prompto.intrinsic.PromptoBinary;
import prompto.type.ImageType;

public class Image extends BinaryValue {

	public Image() {
		super(ImageType.instance());
	}
	
	public Image(PromptoBinary data) {
		super(ImageType.instance(), data);
	}

	public Image(String mimeType, byte[] bytes) {
		this(new PromptoBinary(mimeType, bytes));
	}

}
