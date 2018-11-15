package resources.cwe327brokencrypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CWE327UseBrokenCryptoBasic {

	public CWE327UseBrokenCryptoBasic() {
		super();
	}

	public void bad() throws Throwable {

		String str = "teststring";

		/* FLAW: Insecure cryptographic algorithm (DES) */
		Cipher des = Cipher.getInstance("DES");
		SecretKey key = KeyGenerator.getInstance("DES").generateKey();
		des.init(Cipher.ENCRYPT_MODE, key);

		byte[] enc_str = des.doFinal(str.getBytes());

		System.out.println(new String(enc_str));

	}

	public void good() throws Throwable {
		good1();
	}

	private void good1() throws Throwable {

		String str = "teststring";

		/* FIX: Secure cryptographic algorithm (AES) */
		Cipher aes = Cipher.getInstance("AES");
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(128);
		SecretKey key = kg.generateKey();
		aes.init(Cipher.ENCRYPT_MODE, key);

		byte[] enc_str = aes.doFinal(str.getBytes());

		System.out.println(new String(enc_str));

	}

}
