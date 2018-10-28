package prompto.verifier;

import prompto.compiler.Opcode;

public class RawBytecodeStream {

	byte[] opcodes;
	int _bci; // bci if current bytecode
	int _next_bci; // bci of next bytecode
	int _end_bci; // bci after the current iteration interval

	public RawBytecodeStream(byte[] opcodes) {
		this.opcodes = opcodes;
		_bci = 0;
		_next_bci = 0;
		_end_bci = opcodes.length;
	}

	public byte[] getOpcodes() {
		return opcodes;
	}
	
	public boolean is_last_bytecode() {
		return _next_bci >= _end_bci;
	}

	public int bci() {
		return _bci;
	}

	public Opcode raw_next() {
		Opcode code;
	    // set reading position
	    _bci = _next_bci;
	    if(is_last_bytecode())
	    	throw new VerifierException("caller should check is_last_bytecode()");

	    if(opcodes[_bci]==0xc4) // wide prefix
	    	throw new UnsupportedOperationException();
	    else
	    	code = Opcode.get(opcodes[_bci]);

	    // set next bytecode position
	    int l = code.kind.length;
	    if (l > 0 && (_bci + l) <= _end_bci) {
	      _next_bci += l;
	      return code;
	    } else {
	      return raw_next_special(code);
	    }
	  }

	private Opcode raw_next_special(Opcode code) {
    	throw new UnsupportedOperationException();
	}

	public boolean is_wide() {
		throw new UnsupportedOperationException();
	}

	public short get_index_u2() {
	    Opcode code;
	    if(opcodes[_bci]==0xc4) // wide prefix
	    	throw new UnsupportedOperationException();
	    else
	    	code = Opcode.get(opcodes[_bci]);
	    int _pos = _bci + code.kind.width;
		if(_pos + 2 > opcodes.length)
			throw new VerifierException("End of stream!");
		short value = (short)(opcodes[_pos++] & 0xFF);
		value <<= 8;
		value |= opcodes[_pos] & 0xFF;
		return value;
	}

	public short get_index_u1_at(int offset) {
	    int _pos = offset;
		if(_pos >= opcodes.length)
			throw new VerifierException("End of stream!");
		return (short)(opcodes[_pos] & 0xFF);
	}

	public short get_index_u2_at(int offset) {
	    int _pos = offset;
		if(_pos >= opcodes.length)
			throw new VerifierException("End of stream!");
		short value = (short)(opcodes[_pos++] & 0xFF);
		value <<= 8;
		value |= opcodes[_pos] & 0xFF;
		return value;
	}

	public int dest() {
		return bci() + get_index_u2();	
	}

}
