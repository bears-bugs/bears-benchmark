package prompto.verifier;

import java.util.ArrayList;
import java.util.List;

public class Signature {

	public static Signature parse(String descriptor) {
		String ss = descriptor;
		List<String> types = new ArrayList<>();
		String type = null;
		while(ss.length()>0) {
			switch(ss.charAt(0)) {
			case '(':
				ss = ss.substring(1);
				break;
			case ')':
				if(type!=null) {
					types.add(type);
					type = null;
				}
				ss = ss.substring(1);
				break;
			case '[':
				if(type==null)
					type = "[";
				else
					type += "[";
				ss = ss.substring(1);
				break;
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
			case 'V':
				if(type==null)
					types.add(ss.substring(0, 1));
				else {
					types.add(type + ss.substring(0, 1));
					type = null;
				}
				ss = ss.substring(1);
				break;
			case 'L':
				int idx = ss.indexOf(';');
				if(type==null)
					types.add(ss.substring(0, idx + 1));
				else {
					types.add(type + ss.substring(0, idx + 1));
					type = null;
				}
				ss = ss.substring(idx + 1);
				break;
			default:
				throw new VerifierException("Invalid signature " + descriptor);
			}
		}
		if(type!=null)
			types.add(type);
		return new Signature(types.toArray(new String[types.size()]));
	}

	String[] _types;

	public Signature(String[] types) {
		this._types = types;
	}


}
