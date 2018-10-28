package prompto.compiler;

import java.util.ArrayList;
import java.util.List;

public class BootstrapMethodsAttribute implements IAttribute {

	Utf8Constant attributeName = new Utf8Constant("BootstrapMethods");
	List<BootstrapMethod> methods = new ArrayList<>();
	
	public void addBootstrapMethod(BootstrapMethod method) {
		int idx = methods.indexOf(method);
		if(idx>=0)
			method.setIndexInBootstrapList(idx);
		else {
			method.setIndexInBootstrapList(0);
			methods.add(method);
		}
	}

	@Override
	public void register(ConstantsPool pool) {
		attributeName.register(pool);
		methods.forEach((m)->
			m.register(pool));
		
	}

	@Override
	public int lengthWithoutHeader() {
		/*
		BootstrapMethods_attribute {
		    u2 attribute_name_index;
		    u4 attribute_length;
		    u2 num_bootstrap_methods;
		    {   u2 bootstrap_method_ref;
		        u2 num_bootstrap_arguments;
		        u2 bootstrap_arguments[num_bootstrap_arguments];
		    } bootstrap_methods[num_bootstrap_methods];
		}
		*/
		int length = 2; // num_bootstrap_methods
		for(BootstrapMethod m : methods)
			length += m.length();
		return length;
	}

	@Override
	public void writeTo(ByteWriter writer) {
		writer.writeU2(attributeName.getIndexInConstantPool());
		writer.writeU4(lengthWithoutHeader());
		writer.writeU2(methods.size());
		methods.forEach((m)->
			m.writeTo(writer));
	}



}
