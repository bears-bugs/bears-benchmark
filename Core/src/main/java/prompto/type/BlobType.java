package prompto.type;

import java.lang.reflect.Type;

import prompto.intrinsic.PromptoBinary;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.value.Blob;
import prompto.value.IValue;

public class BlobType extends BinaryType { 

	static BlobType instance = new BlobType();
	
	public static BlobType instance() {
		return instance;
	}
	
	private BlobType() {
		super(Family.BLOB);
	}

	@Override
	public Type getJavaType(Context context) {
		return Blob.class;
	}
	
	@Override
	protected IValue newInstance(PromptoBinary binary) {
		return new Blob(binary);
	}
	
}
