package prompto.compiler;

public class Utf8Constant implements IInternalConstant {
	
	String value;
	int index;
	
	public Utf8Constant(String value) {
		this.value = value;
	}

	@Override
	public int getTag() {
		return Tags.CONSTANT_Utf8;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Utf8Constant 
				&& value.equals(((Utf8Constant)obj).value);
	}
	
	@Override
	public int getIndexInConstantPool() {
		if(index==-1)
			throw new UnsupportedOperationException();
		return index;
	}
	
	@Override
	public void register(ConstantsPool pool) {
		index = pool.registerConstant(this);
	}
	
	@Override
	public void writeTo(ByteWriter writer) {
		writer.writeU1(Tags.CONSTANT_Utf8);
		byte[] utf8 = toModifiedUtf8(value);
		writer.writeU2(utf8.length);
		writer.writeBytes(utf8);
	}

	/* need to transcode to modified UTF-8 */
	/* could not find a transcoder in the JVM so had to write one */
	/* see CONSTANT_Utf8_info at https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html */
	static byte[] toModifiedUtf8(String s) {
		char[] cc = s.toCharArray();
		byte[] bb = new byte[cc.length*6];
		int len = 0;
		for(char c : cc) {
			if(c>=0x0001 && c<=0x007F)
				bb[len++] = (byte)(c & 0x7F);
			else if(c==0x000 || (c>=0x0080 && c<=0x07FF)) {
				// ((x & 0x1f) << 6) + (y & 0x3f)
				bb[len++] = (byte)(0xC0 | ((c >> 6) & 0x1F));
				bb[len++] = (byte)(0x80 | (c & 0x3F));
			} else if(c>=0x0800 && c<=0xFFFF) {
				// ((x & 0xf) << 12) + ((y & 0x3f) << 6) + (z & 0x3f)
				bb[len++] = (byte)(0xE0 | ((c >> 12) & 0x0F));
				bb[len++] = (byte)(0x80 | ((c >> 6) & 0x3F));
				bb[len++] = (byte)(0x80 | (c & 0x3F));
			} else {
				// 0x10000 + ((v & 0x0f) << 16) + ((w & 0x3f) << 10) + ((y & 0x0f) << 6) + (z & 0x3f)
				bb[len++] = (byte)0xED;
				bb[len++] = (byte)(0xA0 | (((c-0x00001) >> 16) & 0x0F));
				bb[len++] = (byte)(0x80 | ((c >> 10) & 0x3F));
				bb[len++] = (byte)0xED;
				bb[len++] = (byte)(0x80 | ((c >> 6) & 0x3F));
				bb[len++] = (byte)(0x80 | (c & 0x3F));
			}
		}
		byte[] res = new byte[len];
		System.arraycopy(bb, 0, res, 0, len);
		return res;
	}
}
