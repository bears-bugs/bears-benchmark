package prompto.compiler;

interface IAttribute {

	void register(ConstantsPool pool);
	void writeTo(ByteWriter writer);
	int lengthWithoutHeader();
	default int lengthWithHeader() {
		/*
		attribute_info {
		    u2 attribute_name_index;
		    u4 attribute_length;
		    u1 info[attribute_length];
		}
		*/
		return 6 + lengthWithoutHeader();
	}
}
