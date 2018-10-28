package prompto.verifier;

public abstract class ArgumentSizeComputer {

	public static int size(String descriptor) {
		String[] types = Signature.parse(descriptor)._types;
		int size = 0;
		for(int i=0;i<types.length-1;i++)
			size += arg_size(types[i]);
		return size;
	}

	private static int arg_size(String arg) {
		if("D".equals(arg) || "J".equals(arg))
			return 2;
		else
			return 1;
	}

}
