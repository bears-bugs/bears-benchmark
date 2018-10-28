package prompto.runtime.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Out {

	static ThreadLocal<PrintStream> oldOut = new ThreadLocal<PrintStream>();
	static ThreadLocal<ByteArrayOutputStream> output  = new ThreadLocal<ByteArrayOutputStream>();
	
	public static void init() {
		oldOut.set(System.out);
		output.set(new ByteArrayOutputStream());
		System.setOut(new PrintStream(output.get()));
	}
	
	public static String read() {
		ByteArrayOutputStream out = output.get();
		String result = out.toString();
		out.reset();
		return result;
	}
	
	public static void reset() {
	}
	
	public static void restore() {
		System.setOut(oldOut.get());
		oldOut.set(null);
	}
}
