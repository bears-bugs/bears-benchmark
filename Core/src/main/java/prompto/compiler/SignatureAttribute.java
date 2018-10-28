package prompto.compiler;

public class SignatureAttribute implements IAttribute {

	Utf8Constant attributeName = new Utf8Constant("Signature");
	Utf8Constant signature;
	
	public SignatureAttribute(String signature) {
		setSignature(signature);
	}

	public String getSignature() {
		return signature.toString();
	}
	
	public void setSignature(String signature) {
		this.signature = new Utf8Constant(signature);
	}

	@Override
	public void register(ConstantsPool pool) {
		attributeName.register(pool);
		signature.register(pool);
	}

	@Override
	public int lengthWithoutHeader() {
		/*
		Signature_attribute {
		    u2 attribute_name_index;
		    u4 attribute_length;
		    u2 signature_index;
		}
		*/
		return 2;
	}

	@Override
	public void writeTo(ByteWriter writer) {
		writer.writeU2(attributeName.getIndexInConstantPool());
		writer.writeU4(lengthWithoutHeader());
		writer.writeU2(signature.getIndexInConstantPool());
	}





}
