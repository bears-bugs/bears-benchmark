package prompto.compiler;

import java.util.ArrayList;
import java.util.List;

public class InnerClassesAttribute implements IAttribute {

	Utf8Constant attributeName = new Utf8Constant("InnerClasses");
	List<InnerClassInfo> classes = new ArrayList<>();
	
	public List<InnerClassInfo> getClasses() {
		return classes;
	}
	
	public void addInnerClass(InnerClassInfo info) {
		classes.add(info);
	}

	@Override
	public void register(ConstantsPool pool) {
		attributeName.register(pool);
		classes.forEach((c)->
			c.register(pool));
		
	}

	@Override
	public int lengthWithoutHeader() {
		/*
		InnerClasses_attribute {
		    u2 attribute_name_index;
		    u4 attribute_length;
		    u2 number_of_classes;
		    {   u2 inner_class_info_index;
		        u2 outer_class_info_index;
		        u2 inner_name_index;
		        u2 inner_class_access_flags;
		    } classes[number_of_classes];
		}
		*/
		return 2 + (8 * classes.size());
	}

	@Override
	public void writeTo(ByteWriter writer) {
		writer.writeU2(attributeName.getIndexInConstantPool());
		writer.writeU4(lengthWithoutHeader());
		writer.writeU2(classes.size());
		classes.forEach((c)->
			c.writeTo(writer));
	}



}
