package prompto.type;

import java.lang.reflect.Type;

import prompto.intrinsic.PromptoBinary;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.value.IValue;
import prompto.value.Image;

public class ImageType extends BinaryType { 

	static ImageType instance = new ImageType();
	
	public static ImageType instance() {
		return instance;
	}
	
	private ImageType() {
		super(Family.IMAGE);
	}

	@Override
	public Type getJavaType(Context context) {
		return Image.class;
	}
	
	@Override
	protected IValue newInstance(PromptoBinary binary) {
		return new Image(binary);
	}

	
}
