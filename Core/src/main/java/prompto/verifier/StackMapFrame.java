package prompto.verifier;

import java.util.Arrays;

import prompto.compiler.MethodInfo;

public class StackMapFrame {

	static final byte FLAG_THIS_UNINIT = 0x01;

	int _offset;

	// See comment in StackMapTable about _frame_count about why these
	// fields are int32_t instead of u2.
	int _locals_size; // number of valid type elements in _locals
	int _stack_size; // number of valid type elements in _stack

	int _stack_mark; // Records the size of the stack prior to an
						// instruction modification, to allow rewinding
						// when/if an error occurs.

	int _max_locals;
	int _max_stack;

	byte _flags;
	VerificationType[] _locals; // local variable type array
	VerificationType[] _stack; // operand stack type array

	ClassVerifier _verifier; // the verifier verifying this method

	public StackMapFrame(int max_locals, int max_stack, ClassVerifier verifier) {
		this._offset = 0;
		this._locals_size = 0;
		this._stack_size = 0;
		this._stack_mark = 0;
		this._flags = 0;
		this._max_locals = max_locals;
		this._max_stack = max_stack;
		this._verifier = verifier;
		this._locals = new VerificationType[max_locals];
		Arrays.fill(_locals, VerificationType.bogus_type);
		this._stack = new VerificationType[max_stack];
		Arrays.fill(_stack, VerificationType.bogus_type);
	}

	public StackMapFrame(int offset, byte flags, int locals_size, int stack_size, int max_locals, int max_stack, VerificationType[] locals, VerificationType[] stack, ClassVerifier verifier) {
		this._offset = offset;
		this._flags = flags;
		this._locals_size = locals_size;
		this._stack_size = stack_size;
		this._stack_mark = -1;
		this._max_locals = max_locals;
		this._max_stack = max_stack;
		this._locals = locals;
		this._stack = stack;
		this._verifier = verifier;
	}

	public void set_locals_size(int locals_size) {
		_locals_size = locals_size;
	}

	public int locals_size() {
		return _locals_size;
	}

	private int max_locals() {
		return _max_locals;
	}

	public void set_stack_size(int stack_size) {
		_stack_size = _stack_mark = stack_size;
	}

	public int stack_size() {
		return _stack_size;
	}

	public VerificationType[] locals() {
		return _locals;
	}

	public VerificationType[] stack() {
		return _stack;
	}
	
	public void set_offset(int offset) {
		_offset = offset;
	}

	public int offset() {
		return _offset;
	}

	public void set_flags(byte flags) {
		_flags = flags;
	}

	public byte flags() {
		return _flags;
	}

	boolean flag_this_uninit() { 
		return (_flags & FLAG_THIS_UNINIT) != 0; 
	}

	private ClassVerifier verifier() {
		return _verifier;
	}

	public void set_mark() {
		// Put bogus type to indicate it's no longer valid.
		if (_stack_mark != -1) {
			for (int i = _stack_mark - 1; i >= _stack_size; --i) {
				_stack[i] = VerificationType.bogus_type;
			}
		}
		_stack_mark = _stack_size;
	}

	public VerificationType set_locals_from_arg(MethodInfo m, VerificationType thisKlass) {
		SignatureStream ss = new SignatureStream(m.getSignature().getValue());
		int init_local_num = 0;
		if (!m.isStatic()) {
			init_local_num++;
			// add one extra argument for instance method
			if ("<init>".equals(m.getName()) && !"java/lang/Object".equals(thisKlass.name())) {
				_locals[0] = VerificationType.uninitialized_this_type;
				_flags |= FLAG_THIS_UNINIT;
			} else {
				_locals[0] = thisKlass;
			}
		}

		// local num may be greater than size of parameters because long/double
		// occupies two slots
		while (!ss.at_return_type()) {
			init_local_num += _verifier.change_sig_to_verificationType(ss, _locals, init_local_num);
			ss.next();
		}
		_locals_size = init_local_num;

		switch (ss.type()) {
		case T_OBJECT:
		case T_ARRAY:
			String sym = ss.as_symbol();
			return VerificationType.reference_type(sym);
		case T_INT:
			return VerificationType.integer_type;
		case T_BYTE:
			return VerificationType.byte_type;
		case T_CHAR:
			return VerificationType.char_type;
		case T_SHORT:
			return VerificationType.short_type;
		case T_BOOLEAN:
			return VerificationType.boolean_type;
		case T_FLOAT:
			return VerificationType.float_type;
		case T_DOUBLE:
			return VerificationType.double_type;
		case T_LONG:
			return VerificationType.long_type;
		case T_VOID:
			return VerificationType.bogus_type;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public void push_stack(VerificationType type) {
		if (type.is_check())
			throw new VerifierException("Must be a real type");
		if (_stack_size >= _max_stack)
			throw new VerifierException("Operand stack overflow");
		_stack[_stack_size++] = type;
	}

	public void push_stack_2(VerificationType type1, VerificationType type2) {
		if (!type1.is_long() && !type1.is_double())
			throw new VerifierException("must be long/double");
		if (!type2.is_long2() && !type2.is_double2())
			throw new VerifierException("must be long/double_2");
		if (_stack_size >= _max_stack - 1) {
			throw new VerifierException("Operand stack overflow");
		}
		_stack[_stack_size++] = type1;
		_stack[_stack_size++] = type2;
	}

	public void copy_locals(StackMapFrame src) {
	  int len = src.locals_size() < _locals_size ? src.locals_size() : _locals_size;
	  for (int i = 0; i < len; i++) {
	    _locals[i] = src.locals()[i];
	  }	
	}

	public VerificationType pop_stack(VerificationType type) {
		if (_stack_size != 0) {
			VerificationType top = _stack[_stack_size - 1];
			boolean subtype = type.is_assignable_from(top, false, verifier());
			if (subtype) {
				--_stack_size;
				return top;
			}
		}
		return pop_stack_ex(type);
	}

	private VerificationType pop_stack_ex(VerificationType type) {
		if (_stack_size <= 0) {
			throw new VerifierException("Operand stack underflow");
		}
		VerificationType top = _stack[--_stack_size];
		boolean subtype = type.is_assignable_from(top, false, verifier());
		if (!subtype) {
			throw new VerifierException("Bad type on operand stack");
		}
		return top;
	}

	public VerificationType pop_stack() {
		throw new UnsupportedOperationException();
	}

	public void set_local(short index, VerificationType type) {
		if (type.is_check())
			throw new VerifierException("Must be a real type");
		if (index >= _max_locals) {
			throw new VerifierException("Local variable table overflow");
		}
		// If type at index is double or long, set the next location to be
		// unusable
		if (_locals[index].is_double() || _locals[index].is_long()) {
			if ((index + 1) >= _locals_size)
				throw new VerifierException("Local variable table overflow");
			_locals[index + 1] = VerificationType.bogus_type;
		}
		// If type at index is double_2 or long_2, set the previous location to
		// be unusable
		if (_locals[index].is_double2() || _locals[index].is_long2()) {
			if (index < 1)
				throw new VerifierException("Local variable table underflow");
			_locals[index - 1] = VerificationType.bogus_type;
		}
		_locals[index] = type;
		if (index >= _locals_size) {
			for (int i = _locals_size; i < index; i++) {
				if (_locals[i] != VerificationType.bogus_type)
					throw new VerifierException("holes must be bogus type");
			}
			_locals_size = index + 1;
		}
	}

	public VerificationType get_local(short index, VerificationType type) {
		if (index >= _max_locals) {
			throw new VerifierException("Local variable table overflow");
		}
		boolean subtype = type.is_assignable_from(_locals[index], false, verifier());
		if (!subtype) {
			throw new VerifierException("Bad local variable type");
		}
		if (index >= _locals_size) {
			_locals_size = index + 1;
		}
		return _locals[index];
	}

	public void initialize_object(VerificationType old_object, VerificationType new_object) {
		for (int i = 0; i < _max_locals; i++) {
			if (_locals[i].equals(old_object)) {
				_locals[i] = new_object;
			}
		}
		for (int i = 0; i < _stack_size; i++) {
			if (_stack[i].equals(old_object)) {
				_stack[i] = new_object;
			}
		}
		if (old_object == VerificationType.uninitialized_this_type) {
			// "this" has been initialized - reset flags
			_flags = 0;
		}
	}

	public void reset() {
		for (int i = 0; i < _max_locals; i++) {
			_locals[i] = VerificationType.bogus_type;
		}
		for (int i = 0; i < _max_stack; i++) {
			_stack[i] = VerificationType.bogus_type;
		}
	}

	public void copy_stack(StackMapFrame src) {
		int len = src.stack_size() < _stack_size ? src.stack_size() : _stack_size;
		  for (int i = 0; i < len; i++) {
		    _stack[i] = src._stack[i];
		  }
	}

	public boolean is_assignable_to(StackMapFrame target, boolean is_exception_handler) {
	  if (_max_locals != target.max_locals()) {
		  throw new VerifierException("Locals size mismatch");
	  }
	  if (_stack_size != target.stack_size()) {
		  throw new VerifierException("Stack size mismatch");
	  }
	  // Only need to compare type elements up to target->locals() or target->stack().
	  // The remaining type elements in this state can be ignored because they are
	  // assignable to bogus type.
	  int mismatch_loc = is_assignable_to(_locals, target.locals(), target.locals_size());
	  if (mismatch_loc != target.locals_size()) {
		  throw new VerifierException("bad type");
	  }
	  mismatch_loc = is_assignable_to(_stack, target.stack(), _stack_size);
	  if (mismatch_loc != _stack_size) {
		  throw new VerifierException("bad type");
	  }

	  boolean match_flags = (_flags | target.flags()) == target.flags();
	  if (match_flags || is_exception_handler && has_flag_match_exception(target)) {
	    return true;
	  } else {
		  throw new VerifierException("bad flags");
	  }
	}

	// Returns the location of the first mismatch, or 'len' if there are no mismatches
	int is_assignable_to(VerificationType[] from, VerificationType[] to, int len) {
	  int i = 0;
	  for (i = 0; i < len; i++) {
	    if (!to[i].is_assignable_from(from[i], false, verifier())) {
	      break;
	    }
	  }
	  return i;
	}

	boolean has_flag_match_exception(StackMapFrame target) {
	  // We allow flags of {UninitThis} to assign to {} if-and-only-if the
	  // target frame does not depend upon the current type.
	  // This is slightly too strict, as we need only enforce that the
	  // slots that were initialized by the <init> (the things that were
	  // UninitializedThis before initialize_object() converted them) are unused.
	  // However we didn't save that information so we'll enforce this upon
	  // anything that might have been initialized.  This is a rare situation
	  // and javac never generates code that would end up here, but some profilers
	  // (such as NetBeans) might, when adding exception handlers in <init>
	  // methods to cover the invokespecial instruction.  See 7020118.

	  if(max_locals() != target.max_locals() || stack_size() != target.stack_size())
		  throw new VerifierException("StackMap sizes must match");

	  VerificationType top = VerificationType.top_type;
	  VerificationType this_type = verifier().current_type();

	  if (!flag_this_uninit() || target.flags() != 0) {
	    return false;
	  }

	  for (int i = 0; i < target.locals_size(); ++i) {
	    if (locals()[i] == this_type && target.locals()[i] != top) {
	      return false;
	    }
	  }

	  for (int i = 0; i < target.stack_size(); ++i) {
	    if (stack()[i] == this_type && target.stack()[i] != top) {
	      return false;
	    }
	  }

	  return true;
	}

}
