package resources.cwe328reversiblehash;

import java.security.MessageDigest;

public class CWE328ReversibleOneWayHashBasic {

	public CWE328ReversibleOneWayHashBasic() {
		super();
	}

	public void badMD5() throws Throwable {

		String input = "Test Input";

		/* FLAW: Insecure cryptographic hashing algorithm (MD5) */
		MessageDigest hash = MessageDigest.getInstance("MD5");
		byte[] hashv = hash.digest(input.getBytes()); /*
													 * INCIDENTAL FLAW:
													 * Hard-coded input to hash
													 * algorithm
													 */

		System.out.println(new String(hashv));

	}
	
	public void badSHA1() throws Throwable {

		String input = "Test Input";

		/* FLAW: Insecure cryptographic hashing algorithm (SHA1) */
		MessageDigest hash = MessageDigest.getInstance("SHA1");
		byte[] hashv = hash.digest(input.getBytes()); /*
													 * INCIDENTAL FLAW:
													 * Hard-coded input to hash
													 * algorithm
													 */

		System.out.println(new String(hashv));

	}

	public void good() throws Throwable {
		good1();
	}

	private void good1() throws Throwable {

		String input = "Test Input";

		/* FIX: Secure cryptographic hashing algorithm (SHA-512) */
		MessageDigest hash = MessageDigest.getInstance("SHA-512");
		byte[] hashv = hash.digest(input.getBytes()); /*
													 * INCIDENTAL FLAW:
													 * Hard-coded input to hash
													 * algorithm
													 */

		System.out.println(new String(hashv));

	}
}
