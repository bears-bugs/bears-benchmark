package prompto.verifier;

public class ByteReader {

	byte[] _data;
	int _pos;
	
	public ByteReader(byte[] data) {
		this._data = data;
		this._pos = 0;
	}

	public ByteReader(byte[] data, int pos) {
		this._data = data;
		this._pos = pos;
	}

	public int get_u1() {
		if(_pos + 2 > _data.length)
			throw new VerifierException("End of stream!");
		return _data[_pos++] & 0xFF;
	}

	public int get_u2() {
		if(_pos + 2 > _data.length)
			throw new VerifierException("End of stream!");
		int value = _data[_pos++] & 0xFF;
		value <<= 8;
		value |= _data[_pos++] & 0xFF;
		return value;
	}

	public boolean at_end() {
		return _pos >= _data.length;
	}


}
