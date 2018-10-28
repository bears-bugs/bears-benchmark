package prompto.intrinsic;


public abstract class PromptoChar {

	public static String multiply(char c, int count) {
        char[] cc = new char[count];
        for (int i = 0; i < count; i++)
            cc[i] = c;
        return new String(cc);
		
	}
	
}
