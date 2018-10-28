package prompto.compiler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Descriptor {

	protected Type[] types;
	protected String descriptor;
	
	@Override
	public String toString() {
		return descriptor;
	}
	
	public Type getLastType() {
		return types[types.length-1]; // last type is field type or result type
	}
	
	public Type[] getTypes() {
		return types;
	}

	public String getLastDescriptor() {
		return CompilerUtils.getDescriptor(getLastType());
	}
	
	public static class Field extends Descriptor {
		
		public Field(Type type) {
			this.types = new Type[] { type };
			this.descriptor = CompilerUtils.getDescriptor(type);
		}
	}

	public static class Method extends Descriptor {
		
		public Method(Type ... types) {
			this.types = types;
			this.descriptor = CompilerUtils.createProto(types);
		}

		public Method(Type[] parameterTypes, Type returnType) {
			List<Type> types = new ArrayList<>();
			types.addAll(Arrays.asList(parameterTypes));
			types.add(returnType);
			this.types = types.toArray(new Type[types.size()]);
			this.descriptor = CompilerUtils.createProto(this.types);
		}
	}


}
