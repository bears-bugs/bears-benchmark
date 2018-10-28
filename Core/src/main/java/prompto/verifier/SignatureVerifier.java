package prompto.verifier;

public abstract class SignatureVerifier {

	public static boolean is_valid_method_signature(String descriptor) {
		try {
			Signature.parse(descriptor);
			return true;
		} catch(VerifierException e) {
			return false;
		}
	}

}
