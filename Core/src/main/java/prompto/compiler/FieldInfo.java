package prompto.compiler;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class FieldInfo {

	int accessFlags = Modifier.PROTECTED;
	Type type;
	Utf8Constant name;
	Utf8Constant desc;
	List<IAttribute> attributes = new LinkedList<>();

	public FieldInfo(String name, Type type) {
		this.type = type;
		this.name = new Utf8Constant(name);
		this.desc = new Utf8Constant(CompilerUtils.getDescriptor(type));
	}
	
	public void addModifier(int modifier) {
		accessFlags |= modifier;
	}
	
	public void clearModifier(int modifier) {
		accessFlags &= ~modifier;
	}

	public Utf8Constant getName() {
		return name;
	}
	
	public Utf8Constant getDescriptor() {
		return desc;
	}

	public Type getType() {
		return type;
	}

	void register(ConstantsPool pool) {
		name.register(pool);
		desc.register(pool);
		attributes.forEach((a)->
			a.register(pool));
	}

	void writeTo(ByteWriter writer) {
		/*
		field_info {
		    u2             access_flags;
		    u2             name_index;
		    u2             descriptor_index;
		    u2             attributes_count;
		    attribute_info attributes[attributes_count];
		}*/
		writer.writeU2(accessFlags);
		writer.writeU2(name.getIndexInConstantPool());
		writer.writeU2(desc.getIndexInConstantPool());
		writer.writeU2(attributes.size());
		attributes.forEach((a)->a.writeTo(writer));
	}




}
