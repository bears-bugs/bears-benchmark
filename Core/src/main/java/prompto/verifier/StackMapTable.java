package prompto.verifier;

public class StackMapTable {

	static final int SAME_LOCALS_1_STACK_ITEM_EXTENDED = 247;
	static final int SAME_EXTENDED = 251;
	static final int FULL = 255;

	int _code_length;
	int _frame_count; // Stackmap frame count
	StackMapFrame[] _frame_array;

	public StackMapTable(StackMapReader reader, StackMapFrame init_frame, int max_locals, int max_stack, byte[] code_data, int code_length) {
		_code_length = code_length;
		_frame_count = reader.get_frame_count();
		if (_frame_count > 0) {
			_frame_array = new StackMapFrame[_frame_count];
			StackMapFrame pre_frame = init_frame;
			for (int i = 0; i < _frame_count; i++) {
				StackMapFrame frame = reader.next(pre_frame, i == 0, max_locals, max_stack);
				_frame_array[i] = frame;
				int offset = frame.offset();
				if (offset >= code_length || code_data[offset] == 0)
					throw new VerifierException("StackMapTable error: bad offset");
				pre_frame = frame;
			}
		}
		reader.check_end();
	}

	public int get_frame_count() {
		return _frame_count;
	}

	boolean match_stackmap(StackMapFrame frame, int target, boolean match, boolean update, boolean handler) {
		int index = get_index_from_offset(target);
		return match_stackmap(frame, target, index, match, update, handler);
	}

	// Match and/or update current_frame to the frame in stackmap table with
	// specified offset and frame index. Return true if the two frames match.
	// handler is true if the frame in stackmap_table is for an exception
	// handler.
	//
	// The values of match and update are: _match__update__handler
	//
	// checking a branch target: true false false
	// checking an exception handler: true false true
	// linear bytecode verification following an
	// unconditional branch: false true false
	// linear bytecode verification not following an
	// unconditional branch: true true false
	public boolean match_stackmap(StackMapFrame frame, int target, int frame_index, boolean match, boolean update, boolean handler) {
		if (frame_index < 0 || frame_index >= _frame_count) {
			throw new VerifierException("Expecting a stackmap frame at branch target " + target);
		}
		StackMapFrame stackmap_frame = _frame_array[frame_index];
		boolean result = true;
		if (match) {
			// Has direct control flow from last instruction, need to match the
			// two frames.
			if (!frame.is_assignable_to(stackmap_frame, handler))
				throw new VerifierException("Instruction type does not match stack map");
		}
		if (update) {
			// Use the frame in stackmap table as current frame
			int lsize = stackmap_frame.locals_size();
			int ssize = stackmap_frame.stack_size();
			if (frame.locals_size() > lsize || frame.stack_size() > ssize) {
				// Make sure unused type array items are all _bogus_type.
				frame.reset();
			}
			frame.set_locals_size(lsize);
			frame.copy_locals(stackmap_frame);
			frame.set_stack_size(ssize);
			frame.copy_stack(stackmap_frame);
			frame.set_flags(stackmap_frame.flags());
		}
		return result;
	}

	private int get_index_from_offset(int offset) {
		int i = 0;
		for (; i < _frame_count; i++) {
			if (_frame_array[i].offset() == offset) {
				return i;
			}
		}
		return i; // frame with offset doesn't exist in the array
	}

	public int get_offset(int stackmap_index) {
		return _frame_array[stackmap_index].offset();
	}

	public void check_jump_target(StackMapFrame frame, int target) {
		boolean match = match_stackmap(frame, target, true, false, false);
		if (!match || (target < 0 || target >= _code_length)) {
			throw new VerifierException("Inconsistent stackmap frames at branch target " + target);
		}
	}

}
