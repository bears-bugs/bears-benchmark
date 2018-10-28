package prompto.verifier;

import java.util.concurrent.atomic.AtomicInteger;

import prompto.compiler.ClassConstant;
import prompto.compiler.IConstantOperand;
import prompto.compiler.MethodInfo;

public class StackMapReader {

	ClassVerifier _verifier;
	ByteReader _stream;
	byte[] _code_data;
	int _code_length;
	int _frame_count;
	
	public StackMapReader(ClassVerifier verifier, MethodInfo method, byte[] code_data, int code_length) {
		 this._verifier = verifier;
		 this._code_data = code_data;
		 this._code_length = code_length;
		 if(method.getCodeAttribute().getStackMapTable()!=null) {
			  byte[] stackmap_data = method.getCodeAttribute().getStackMapTable().getData();
			  _stream = new ByteReader(stackmap_data, 6);
			  _frame_count = _stream.get_u2();
		 } else {
			 // There's no stackmap table present. Frame count and size are 0.
			 _frame_count = 0;
		 }
	}

	public int get_frame_count() {
		return _frame_count;
	}

	public StackMapFrame next(StackMapFrame pre_frame, boolean first, int max_locals, int max_stack) {
		  StackMapFrame frame;
		  int offset;
		  VerificationType[] locals = null;
		  int frame_type = _stream.get_u1();
		  if (frame_type < 64) {
		    // same_frame
		    if (first) {
		      offset = frame_type;
		      // Can't share the locals array since that is updated by the verifier.
		      if (pre_frame.locals_size() > 0) {
		        locals = new VerificationType[pre_frame.locals_size()];
		      }
		    } else {
		      offset = pre_frame.offset() + frame_type + 1;
		      locals = pre_frame.locals();
		    }
		    frame = new StackMapFrame(
		      offset, pre_frame.flags(), pre_frame.locals_size(), 0,
		      max_locals, max_stack, locals, null, _verifier);
		    if (first && locals != null) {
		      frame.copy_locals(pre_frame);
		    }
		    return frame;
		  }
		  if (frame_type < 128) {
		    // same_locals_1_stack_item_frame
		    if (first) {
		      offset = frame_type - 64;
		      // Can't share the locals array since that is updated by the verifier.
		      if (pre_frame.locals_size() > 0) {
		        locals = new VerificationType[pre_frame.locals_size()];
		      }
		    } else {
		      offset = pre_frame.offset() + frame_type - 63;
		      locals = pre_frame.locals();
		    }
		    VerificationType[] stack = new VerificationType[2];
		    int stack_size = 1;
		    stack[0] = parse_verification_type(null);
		    if (stack[0].is_category2()) {
		      stack[1] = stack[0].to_category2_2nd();
		      stack_size = 2;
		    }
		    check_verification_type_array_size( stack_size, max_stack);
		    frame = new StackMapFrame(
		      offset, pre_frame.flags(), pre_frame.locals_size(), stack_size,
		      max_locals, max_stack, locals, stack, _verifier);
		    if (first && locals != null) {
		      frame.copy_locals(pre_frame);
		    }
		    return frame;
		  }

		  int offset_delta = _stream.get_u2();

		  if (frame_type < StackMapTable.SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
			  throw new VerifierException("reserved frame type");
		  }

		  if (frame_type == StackMapTable.SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
		    // same_locals_1_stack_item_frame_extended
		    if (first) {
		      offset = offset_delta;
		      // Can't share the locals array since that is updated by the verifier.
		      if (pre_frame.locals_size() > 0) {
		        locals = new VerificationType[pre_frame.locals_size()];
		      }
		    } else {
		      offset = pre_frame.offset() + offset_delta + 1;
		      locals = pre_frame.locals();
		    }
		    VerificationType[] stack = new VerificationType[2];
		    int stack_size = 1;
		    stack[0] = parse_verification_type(null);
		    if (stack[0].is_category2()) {
		      stack[1] = stack[0].to_category2_2nd();
		      stack_size = 2;
		    }
		    check_verification_type_array_size(stack_size, max_stack);
		    frame = new StackMapFrame(
		      offset, pre_frame.flags(), pre_frame.locals_size(), stack_size,
		      max_locals, max_stack, locals, stack, _verifier);
		    if (first && locals != null) {
		      frame.copy_locals(pre_frame);
		    }
		    return frame;
		  }

		  if (frame_type <= StackMapTable.SAME_EXTENDED) {
		    // chop_frame or same_frame_extended
		    locals = pre_frame.locals();
		    int length = pre_frame.locals_size();
		    int chops = StackMapTable.SAME_EXTENDED - frame_type;
		    int new_length = length;
		    byte flags = pre_frame.flags();
		    if (chops != 0) {
		      new_length = chop(locals, length, chops);
		      check_verification_type_array_size(new_length, max_locals);
		      // Recompute flags since uninitializedThis could have been chopped.
		      flags = 0;
		      for (int i=0; i<new_length; i++) {
		        if (locals[i].is_uninitialized_this()) {
		          flags |= StackMapFrame.FLAG_THIS_UNINIT;
		          break;
		        }
		      }
		    }
		    if (first) {
		      offset = offset_delta;
		      // Can't share the locals array since that is updated by the verifier.
		      if (new_length > 0) {
		        locals = new VerificationType[new_length];
		      } else {
		        locals = null;
		      }
		    } else {
		      offset = pre_frame.offset() + offset_delta + 1;
		    }
		    frame = new StackMapFrame(
		      offset, flags, new_length, 0, max_locals, max_stack,
		      locals, null, _verifier);
		    if (first && locals != null) {
		      frame.copy_locals(pre_frame);
		    }
		    return frame;
		  } else if (frame_type < StackMapTable.SAME_EXTENDED + 4) {
		    // append_frame
		    int appends = frame_type - StackMapTable.SAME_EXTENDED;
		    int real_length = pre_frame.locals_size();
		    int new_length = real_length + appends*2;
		    locals = new VerificationType[new_length];
		    VerificationType[] pre_locals = pre_frame.locals();
		    int i;
		    for (i=0; i<pre_frame.locals_size(); i++) {
		      locals[i] = pre_locals[i];
		    }
		    AtomicInteger flags = new AtomicInteger(pre_frame.flags());
		    for (i=0; i<appends; i++) {
		      locals[real_length] = parse_verification_type(flags); // TODO flags as ptr
		      if (locals[real_length].is_category2()) {
		        locals[real_length + 1] = locals[real_length].to_category2_2nd();
		        ++real_length;
		      }
		      ++real_length;
		    }
		    check_verification_type_array_size(real_length, max_locals);
		    if (first) {
		      offset = offset_delta;
		    } else {
		      offset = pre_frame.offset() + offset_delta + 1;
		    }
		    frame = new StackMapFrame(
		      offset, flags.byteValue(), real_length, 0, max_locals,
		      max_stack, locals, null, _verifier);
		    return frame;
		  }
		  if (frame_type == StackMapTable.FULL) {
		    // full_frame
			 AtomicInteger flags = new AtomicInteger(0);
		    int locals_size = _stream.get_u2();
		    int real_locals_size = 0;
		    if (locals_size > 0) {
		      locals = new VerificationType[locals_size*2];
		    }
		    int i;
		    for (i=0; i<locals_size; i++) {
		      locals[real_locals_size] = parse_verification_type(flags); // TODO flags as ptr
		      if (locals[real_locals_size].is_category2()) {
		        locals[real_locals_size + 1] =
		          locals[real_locals_size].to_category2_2nd();
		        ++real_locals_size;
		      }
		      ++real_locals_size;
		    }
		    check_verification_type_array_size(real_locals_size, max_locals);
		    int stack_size = _stream.get_u2();
		    int real_stack_size = 0;
		    VerificationType[] stack = null;
		    if (stack_size > 0) {
		      stack = new VerificationType[stack_size*2];
		    }
		    for (i=0; i<stack_size; i++) {
		      stack[real_stack_size] = parse_verification_type(null);
		      if (stack[real_stack_size].is_category2()) {
		        stack[real_stack_size + 1] = stack[real_stack_size].to_category2_2nd();
		        ++real_stack_size;
		      }
		      ++real_stack_size;
		    }
		    check_verification_type_array_size(real_stack_size, max_stack);
		    if (first) {
		      offset = offset_delta;
		    } else {
		      offset = pre_frame.offset() + offset_delta + 1;
		    }
		    frame = new StackMapFrame(
		      offset, flags.byteValue(), real_locals_size, real_stack_size,
		      max_locals, max_stack, locals, stack, _verifier);
		    return frame;
		  }
		  
		  throw new VerifierException("reserved frame type");
	}

	private int chop(VerificationType[] locals, int length, int chops) {
		throw new UnsupportedOperationException();
	}

	private void check_verification_type_array_size(int size, int max_size) {
	   if (size < 0 || size > max_size)
	      // Since this error could be caused someone rewriting the method
	      // but not knowing to update the stackmap data, we call the the
	      // verifier's error method, which may not throw an exception and
	      // failover to the old verifier instead.
	      throw new VerifierException("StackMapTable format error: bad type array size");
	}

	private VerificationType parse_verification_type(AtomicInteger flags) {
		  int tag = _stream.get_u1();
		  if (tag < VerificationType.ITEM_UninitializedThis) {
		    return VerificationType.from_tag(tag);
		  }
		  if (tag == VerificationType.ITEM_Object) {
		    int class_index = _stream.get_u2();
		    IConstantOperand operand = _verifier.constantWithIndex(class_index);
		    if(!(operand instanceof ClassConstant))
		    	throw new VerifierException("bad class index");
		    return VerificationType.reference_type(((ClassConstant)operand).getClassName().getValue());
		  }
		  if (tag == VerificationType.ITEM_UninitializedThis) {
		    if (flags != null) {
		      flags.set(flags.byteValue() | StackMapFrame.FLAG_THIS_UNINIT);
		    }
		    return VerificationType.uninitialized_this_type;
		  }
		  if (tag == VerificationType.ITEM_Uninitialized) {
		    int offset = _stream.get_u2();
		    if (offset >= _code_length ||
		        _code_data[offset] != ClassVerifier.NEW_OFFSET) {
		      throw new VerifierException("StackMapTable format error: bad offset for Uninitialized");
		    }
		    return VerificationType.uninitialized_type((short)offset);
		  }
		  throw new VerifierException("bad verification type");
		 }

	public void check_end() {
		if(_stream!=null && !_stream.at_end())
			throw new VerifierException("wrong attribute size");
		
	}

}
