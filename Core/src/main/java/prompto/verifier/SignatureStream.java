package prompto.verifier;


public class SignatureStream {
	
	Signature _sig;
	int _pos;
	
	public SignatureStream(String value) {
		_sig = Signature.parse(value);
	}


	public boolean at_return_type() {
		return _pos >= _sig._types.length - 1;
	}

	public BasicType type() {
		return type(name());
	}
	
	public String as_symbol() {
		String name = name();
		return name.startsWith("L") && name.endsWith(";") ? name.substring(1, name.length()-1) : name;
	}

	private String name() {
		return _pos<_sig._types.length ? _sig._types[_pos] : _sig._types[_sig._types.length-1];
	}

	public void next() {
		_pos++;
	}
	
	private BasicType type(String name) {
		switch(name.charAt(0)) {
	      case 'B': 
	    	  return BasicType.T_BYTE;
	      case 'C': 
	    	  return BasicType.T_CHAR;
	      case 'D': 
	    	  return BasicType.T_DOUBLE;
	      case 'F':
	    	  return BasicType.T_FLOAT;
	      case 'I':
	    	  return BasicType.T_INT;
	      case 'J':
	    	  return BasicType.T_LONG;
	      case 'S': 
	    	  return BasicType.T_SHORT;
	      case 'Z':
	    	  return BasicType.T_BOOLEAN;
	      case 'V': 
	    	  return BasicType.T_VOID;
	      case 'L':
	    	  return BasicType.T_OBJECT;
	      case '[':
	    	  return BasicType.T_ARRAY;
	      default:
	    	  throw new UnsupportedOperationException();
		}
	}



}
