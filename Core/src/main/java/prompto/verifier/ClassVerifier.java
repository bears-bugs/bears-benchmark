package prompto.verifier;

import java.util.concurrent.atomic.AtomicInteger;

import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.FieldConstant;
import prompto.compiler.IConstantOperand;
import prompto.compiler.IOperand;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.Tags;

/* producing valid stack frames is a challenging task */
/* the JVM verifier raises inconsistent stack fram exceptions, but not with enough details */
/* this Verifier class mimics the JVM verifier to help diagnose issues */
/* the corresponding stack frame verifier code is transcribed from the jdk C++ file */
/* http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/87ee5ee27509/src/share/vm/classfile/verifier.cpp */

public class ClassVerifier {

	static final byte BYTECODE_OFFSET = 1;
	static final byte NEW_OFFSET = 2;

	Klass _klass;
	VerificationType _this_type;

	public ClassVerifier(ClassFile classFile) {
		this._klass = new Klass(classFile);
		this._this_type = VerificationType.reference_type(_klass.name());
	}

	public void verify() {
		_klass.classFile.getMethods().forEach((m) -> verifyMethod(m));
	}

	public VerificationType current_type() {
		return _this_type;
	}

	@SuppressWarnings("unused")
	private void verifyMethod(MethodInfo m) {
		int max_stack = m.getCodeAttribute().getStackMapTable().getMaxStack();
		int max_locals = m.getCodeAttribute().getStackMapTable().getMaxLocals();
		// Initial stack map frame: offset is 0, stack is initially empty.
		StackMapFrame current_frame = new StackMapFrame(max_locals, max_stack, this);
		// Set initial locals
		VerificationType return_type = current_frame.set_locals_from_arg(m, current_type());

		int stackmap_index = 0; // index to the stackmap array

		int code_length = m.getCodeAttribute().getOpcodes().length;

		// Scan the bytecode and map each instruction's start offset to a
		// number.
		byte[] code_data = generate_code_data(m, code_length);

		int ex_min = code_length;
		int ex_max = -1;

		/*
		 * // Look through each item on the exception table. Each of the fields
		 * must refer // to a legal instruction. verify_exception_handler_table(
		 * code_length, code_data, ex_min, ex_max);
		 * 
		 * // Look through each entry on the local variable table and make sure
		 * // its range of code array offsets is valid. (4169817) if
		 * (m->has_localvariable_table()) {
		 * verify_local_variable_table(code_length, code_data); }
		 */

		StackMapReader reader = new StackMapReader(this, m, code_data, code_length);
		StackMapTable stackmap_table = new StackMapTable(reader, current_frame, max_locals, max_stack, code_data, code_length);

		RawBytecodeStream bcs = new RawBytecodeStream(m.getCodeAttribute().getOpcodes());

		// Scan the byte code linearly from the start to the end
		boolean no_control_flow = false; // Set to true when there is no direct
											// control
											// flow from current instruction to
											// the next
											// instruction in sequence

		Opcode opcode;
		while (!bcs.is_last_bytecode()) {
			opcode = bcs.raw_next();
			int bci = bcs.bci();

			// Set current frame's offset to bci
			current_frame.set_offset(bci);
			current_frame.set_mark();

			// Make sure every offset in stackmap table point to the beginning
			// to
			// an instruction. Match current_frame to stackmap_table entry with
			// the same offset if exists.
			stackmap_index = verify_stackmap_table(stackmap_index, bci, current_frame, stackmap_table, no_control_flow);

			AtomicInteger this_uninit = new AtomicInteger(0); // Set to true
																// when
																// invokespecial
																// <init>
																// initialized
																// 'this'

			// Merge with the next instruction
			{
				short index;
				int target;
				VerificationType type, type2;
				VerificationType atype;

				switch (opcode) {
				case NOP:
					no_control_flow = false;
					break;
				case ACONST_NULL: 
					current_frame.push_stack(VerificationType.null_type); 
					no_control_flow = false; 
					break;
				case ICONST_M1:
				case ICONST_0:
				case ICONST_1:
				case ICONST_2:
				case ICONST_3:
				case ICONST_4:
				case ICONST_5:
					current_frame.push_stack(VerificationType.integer_type);
					no_control_flow = false;
					break; /*
							 * case lconst_0 : case lconst_1 :
							 * current_frame.push_stack_2(
							 * VerificationType.long_type,
							 * VerificationType.long2_type); no_control_flow =
							 * false; break; case fconst_0 : case fconst_1 :
							 * case fconst_2 : current_frame.push_stack(
							 * VerificationType.float_type); no_control_flow =
							 * false; break; case dconst_0 : case dconst_1 :
							 * current_frame.push_stack_2(
							 * VerificationType.double_type,
							 * VerificationType.double2_type); no_control_flow =
							 * false; break; case sipush : case bipush :
							 * current_frame.push_stack(
							 * VerificationType.integer_type); no_control_flow =
							 * false; break; case ldc : verify_ldc( opcode,
							 * bcs.get_index_u1(), &current_frame, cp, bci);
							 * no_control_flow = false; break; case ldc_w : case
							 * ldc2_w : verify_ldc( opcode, bcs.get_index_u2(),
							 * &current_frame, cp, bci); no_control_flow =
							 * false; break; case iload :
							 * verify_iload(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case iload_0 :
							 * case iload_1 : case iload_2 : case iload_3 :
							 * index = opcode - Opcode.iload_0;
							 * verify_iload(index, &current_frame);
							 * no_control_flow = false; break; case lload :
							 * verify_lload(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case lload_0 :
							 * case lload_1 : case lload_2 : case lload_3 :
							 * index = opcode - Opcode.lload_0;
							 * verify_lload(index, &current_frame);
							 * no_control_flow = false; break; case fload :
							 * verify_fload(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case fload_0 :
							 * case fload_1 : case fload_2 : case fload_3 :
							 * index = opcode - Opcode.fload_0;
							 * verify_fload(index, &current_frame);
							 * no_control_flow = false; break; case dload :
							 * verify_dload(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case dload_0 :
							 * case dload_1 : case dload_2 : case dload_3 :
							 * index = opcode - Opcode.dload_0;
							 * verify_dload(index, &current_frame);
							 * no_control_flow = false; break; case aload :
							 * verify_aload(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break;
							 */
				case ALOAD_0:
				case ALOAD_1:
				case ALOAD_2:
				case ALOAD_3:
					index = (short) (opcode.ordinal() - Opcode.ALOAD_0.ordinal());
					verify_aload(index, current_frame);
					no_control_flow = false;
					break; /*
							 * case iaload : type = current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_int_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[I",
							 * THREAD)), bad_type_msg, "iaload"); return; }
							 * current_frame.push_stack(
							 * VerificationType.integer_type); no_control_flow =
							 * false; break; case baload : type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_bool_array() &&
							 * !atype.is_byte_array()) { verify_error(
							 * ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx()), bad_type_msg,
							 * "baload"); return; } current_frame.push_stack(
							 * VerificationType.integer_type); no_control_flow =
							 * false; break; case caload : type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_char_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[C",
							 * THREAD)), bad_type_msg, "caload"); return; }
							 * current_frame.push_stack(
							 * VerificationType.integer_type); no_control_flow =
							 * false; break; case saload : type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_short_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[S",
							 * THREAD)), bad_type_msg, "saload"); return; }
							 * current_frame.push_stack(
							 * VerificationType.integer_type); no_control_flow =
							 * false; break; case laload : type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_long_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[J",
							 * THREAD)), bad_type_msg, "laload"); return; }
							 * current_frame.push_stack_2(
							 * VerificationType.long_type,
							 * VerificationType.long2_type); no_control_flow =
							 * false; break; case faload : type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_float_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[F",
							 * THREAD)), bad_type_msg, "faload"); return; }
							 * current_frame.push_stack(
							 * VerificationType.float_type); no_control_flow =
							 * false; break; case daload : type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_double_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[D",
							 * THREAD)), bad_type_msg, "daload"); return; }
							 * current_frame.push_stack_2(
							 * VerificationType.double_type,
							 * VerificationType.double2_type); no_control_flow =
							 * false; break; case aaload : { type =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_reference_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(),
							 * TypeOrigin::implicit
							 * (VerificationType::reference_check())),
							 * bad_type_msg, "aaload"); return; } if
							 * (atype.is_null()) { current_frame.push_stack(
							 * VerificationType.null_type); } else {
							 * VerificationType component =
							 * atype.get_component(this);
							 * current_frame.push_stack(component); }
							 * no_control_flow = false; break; } case istore :
							 * verify_istore(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case istore_0 :
							 * case istore_1 : case istore_2 : case istore_3 :
							 * index = opcode - Opcode.istore_0;
							 * verify_istore(index, &current_frame);
							 * no_control_flow = false; break; case lstore :
							 * verify_lstore(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case lstore_0 :
							 * case lstore_1 : case lstore_2 : case lstore_3 :
							 * index = opcode - Opcode.lstore_0;
							 * verify_lstore(index, &current_frame);
							 * no_control_flow = false; break; case fstore :
							 * verify_fstore(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case fstore_0 :
							 * case fstore_1 : case fstore_2 : case fstore_3 :
							 * index = opcode - Opcode.fstore_0;
							 * verify_fstore(index, &current_frame);
							 * no_control_flow = false; break; case dstore :
							 * verify_dstore(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break; case dstore_0 :
							 * case dstore_1 : case dstore_2 : case dstore_3 :
							 * index = opcode - Opcode.dstore_0;
							 * verify_dstore(index, &current_frame);
							 * no_control_flow = false; break; case astore :
							 * verify_astore(bcs.get_index(), &current_frame);
							 * no_control_flow = false; break;
							 */
				case ASTORE_0:
				case ASTORE_1:
				case ASTORE_2:
				case ASTORE_3:
					index = (short) (opcode.ordinal() - Opcode.ASTORE_0.ordinal());
					verify_astore(index, current_frame);
					no_control_flow = false;
					break; /*
							 * case iastore : type = current_frame.pop_stack(
							 * VerificationType.integer_type); type2 =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_int_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[I",
							 * THREAD)), bad_type_msg, "iastore"); return; }
							 * no_control_flow = false; break; case bastore :
							 * type = current_frame.pop_stack(
							 * VerificationType.integer_type); type2 =
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_bool_array() &&
							 * !atype.is_byte_array()) { verify_error(
							 * ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx()), bad_type_msg,
							 * "bastore"); return; } no_control_flow = false;
							 * break; case castore : current_frame.pop_stack(
							 * VerificationType.integer_type);
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_char_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[C",
							 * THREAD)), bad_type_msg, "castore"); return; }
							 * no_control_flow = false; break; case sastore :
							 * current_frame.pop_stack(
							 * VerificationType.integer_type);
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_short_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[S",
							 * THREAD)), bad_type_msg, "sastore"); return; }
							 * no_control_flow = false; break; case lastore :
							 * current_frame.pop_stack_2(
							 * VerificationType.long2_type,
							 * VerificationType.long_type);
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_long_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[J",
							 * THREAD)), bad_type_msg, "lastore"); return; }
							 * no_control_flow = false; break; case fastore :
							 * current_frame.pop_stack(
							 * VerificationType.float_type);
							 * current_frame.pop_stack
							 * (VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_float_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[F",
							 * THREAD)), bad_type_msg, "fastore"); return; }
							 * no_control_flow = false; break; case dastore :
							 * current_frame.pop_stack_2(
							 * VerificationType.double2_type,
							 * VerificationType.double_type);
							 * current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); if
							 * (!atype.is_double_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(), ref_ctx("[D",
							 * THREAD)), bad_type_msg, "dastore"); return; }
							 * no_control_flow = false; break; case aastore :
							 * type = current_frame.pop_stack(object_type());
							 * type2 = current_frame.pop_stack(
							 * VerificationType.integer_type); atype =
							 * current_frame.pop_stack(
							 * VerificationType::reference_check()); // more
							 * type-checking is done at runtime if
							 * (!atype.is_reference_array()) {
							 * verify_error(ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx(),
							 * TypeOrigin::implicit
							 * (VerificationType::reference_check())),
							 * bad_type_msg, "aastore"); return; } // 4938384:
							 * relaxed constraint in JVMS 3nd edition.
							 * no_control_flow = false; break; case pop :
							 * current_frame.pop_stack(
							 * VerificationType::category1_check());
							 * no_control_flow = false; break; case pop2 : type
							 * = current_frame.pop_stack(CHECK_VERIFY(this)); if
							 * (type.is_category1()) { current_frame.pop_stack(
							 * VerificationType::category1_check()); } else if
							 * (type.is_category2_2nd()) {
							 * current_frame.pop_stack(
							 * VerificationType::category2_check()); } else { /*
							 * Unreachable? Would need a category2_1st on TOS
							 * which does not appear possible.
							 *//*
								 * verify_error( ErrorContext::bad_type(bci,
								 * current_frame.stack_top_ctx()), bad_type_msg,
								 * "pop2"); return; } no_control_flow = false;
								 * break;
								 */
				case DUP:
					type = current_frame.pop_stack(VerificationType.category1_check());
					current_frame.push_stack(type);
					current_frame.push_stack(type);
					no_control_flow = false;
					break;
				/*
				 * case dup_x1 : type = current_frame.pop_stack(
				 * VerificationType::category1_check()); type2 =
				 * current_frame.pop_stack(
				 * VerificationType::category1_check());
				 * current_frame.push_stack(type);
				 * current_frame.push_stack(type2);
				 * current_frame.push_stack(type); no_control_flow = false;
				 * break; case dup_x2 : { VerificationType type3; type =
				 * current_frame.pop_stack(
				 * VerificationType::category1_check()); type2 =
				 * current_frame.pop_stack(CHECK_VERIFY(this)); if
				 * (type2.is_category1()) { type3 = current_frame.pop_stack(
				 * VerificationType::category1_check()); } else if
				 * (type2.is_category2_2nd()) { type3 = current_frame.pop_stack(
				 * VerificationType::category2_check()); } else { /*
				 * Unreachable? Would need a category2_1st at stack depth 2 with
				 * a category1 on TOS which does not appear possible.
				 *//*
					 * verify_error(ErrorContext::bad_type( bci,
					 * current_frame.stack_top_ctx()), bad_type_msg, "dup_x2");
					 * return; } current_frame.push_stack(type);
					 * current_frame.push_stack(type3);
					 * current_frame.push_stack(type2);
					 * current_frame.push_stack(type); no_control_flow = false;
					 * break; } case dup2 : type =
					 * current_frame.pop_stack(CHECK_VERIFY(this)); if
					 * (type.is_category1()) { type2 = current_frame.pop_stack(
					 * VerificationType::category1_check()); } else if
					 * (type.is_category2_2nd()) { type2 =
					 * current_frame.pop_stack(
					 * VerificationType::category2_check()); } else { /*
					 * Unreachable? Would need a category2_1st on TOS which does
					 * not appear possible.
					 *//*
						 * verify_error( ErrorContext::bad_type(bci,
						 * current_frame.stack_top_ctx()), bad_type_msg,
						 * "dup2"); return; } current_frame.push_stack(type2);
						 * current_frame.push_stack(type);
						 * current_frame.push_stack(type2);
						 * current_frame.push_stack(type); no_control_flow =
						 * false; break; case dup2_x1 : { VerificationType
						 * type3; type =
						 * current_frame.pop_stack(CHECK_VERIFY(this)); if
						 * (type.is_category1()) { type2 =
						 * current_frame.pop_stack(
						 * VerificationType::category1_check()); } else if
						 * (type.is_category2_2nd()) { type2 =
						 * current_frame.pop_stack(
						 * VerificationType::category2_check()); } else { /*
						 * Unreachable? Would need a category2_1st on TOS which
						 * does not appear possible.
						 *//*
							 * verify_error( ErrorContext::bad_type(bci,
							 * current_frame.stack_top_ctx()), bad_type_msg,
							 * "dup2_x1"); return; } type3 =
							 * current_frame.pop_stack(
							 * VerificationType::category1_check());
							 * current_frame.push_stack(type2);
							 * current_frame.push_stack(type);
							 * current_frame.push_stack(type3);
							 * current_frame.push_stack(type2);
							 * current_frame.push_stack(type); no_control_flow =
							 * false; break; } case dup2_x2 : { VerificationType
							 * type3, type4; type =
							 * current_frame.pop_stack(CHECK_VERIFY(this)); if
							 * (type.is_category1()) { type2 =
							 * current_frame.pop_stack(
							 * VerificationType::category1_check()); } else if
							 * (type.is_category2_2nd()) { type2 =
							 * current_frame.pop_stack(
							 * VerificationType::category2_check()); } else { /*
							 * Unreachable? Would need a category2_1st on TOS
							 * which does not appear possible.
							 *//*
								 * verify_error( ErrorContext::bad_type(bci,
								 * current_frame.stack_top_ctx()), bad_type_msg,
								 * "dup2_x2"); return; } type3 =
								 * current_frame.pop_stack(CHECK_VERIFY(this));
								 * if (type3.is_category1()) { type4 =
								 * current_frame.pop_stack(
								 * VerificationType::category1_check()); } else
								 * if (type3.is_category2_2nd()) { type4 =
								 * current_frame.pop_stack(
								 * VerificationType::category2_check()); } else
								 * { /* Unreachable? Would need a category2_1st
								 * on TOS after popping a long/double or two
								 * category 1's, which does not appear possible.
								 */
				/*
				 * verify_error( ErrorContext::bad_type(bci,
				 * current_frame.stack_top_ctx()), bad_type_msg, "dup2_x2");
				 * return; } current_frame.push_stack(type2);
				 * current_frame.push_stack(type);
				 * current_frame.push_stack(type4);
				 * current_frame.push_stack(type3);
				 * current_frame.push_stack(type2);
				 * current_frame.push_stack(type); no_control_flow = false;
				 * break; } case swap : type = current_frame.pop_stack(
				 * VerificationType::category1_check()); type2 =
				 * current_frame.pop_stack(
				 * VerificationType::category1_check());
				 * current_frame.push_stack(type);
				 * current_frame.push_stack(type2); no_control_flow = false;
				 * break; case iadd : case isub : case imul : case idiv : case
				 * irem : case ishl : case ishr : case iushr : case ior : case
				 * ixor : case iand : current_frame.pop_stack(
				 * VerificationType.integer_type); // fall through case ineg :
				 * current_frame.pop_stack( VerificationType.integer_type);
				 * current_frame.push_stack( VerificationType.integer_type);
				 * no_control_flow = false; break; case ladd : case lsub : case
				 * lmul : case ldiv : case lrem : case land : case lor : case
				 * lxor : current_frame.pop_stack_2(
				 * VerificationType.long2_type, VerificationType.long_type); //
				 * fall through case lneg : current_frame.pop_stack_2(
				 * VerificationType.long2_type, VerificationType.long_type);
				 * current_frame.push_stack_2( VerificationType.long_type,
				 * VerificationType.long2_type); no_control_flow = false; break;
				 * case lshl : case lshr : case lushr : current_frame.pop_stack(
				 * VerificationType.integer_type); current_frame.pop_stack_2(
				 * VerificationType.long2_type, VerificationType.long_type);
				 * current_frame.push_stack_2( VerificationType.long_type,
				 * VerificationType.long2_type); no_control_flow = false; break;
				 * case fadd : case fsub : case fmul : case fdiv : case frem :
				 * current_frame.pop_stack( VerificationType.float_type); //
				 * fall through case fneg : current_frame.pop_stack(
				 * VerificationType.float_type); current_frame.push_stack(
				 * VerificationType.float_type); no_control_flow = false; break;
				 * case dadd : case dsub : case dmul : case ddiv : case drem :
				 * current_frame.pop_stack_2( VerificationType.double2_type,
				 * VerificationType.double_type); // fall through case dneg :
				 * current_frame.pop_stack_2( VerificationType.double2_type,
				 * VerificationType.double_type); current_frame.push_stack_2(
				 * VerificationType.double_type, VerificationType.double2_type);
				 * no_control_flow = false; break; case iinc :
				 * verify_iinc(bcs.get_index(), &current_frame); no_control_flow
				 * = false; break; */
				case I2L: 
					type = current_frame.pop_stack(VerificationType.integer_type); 
					current_frame.push_stack_2(VerificationType.long_type, VerificationType.long2_type);
				 	no_control_flow = false; break; 
				 /*case l2i :
				 * current_frame.pop_stack_2( VerificationType.long2_type,
				 * VerificationType.long_type); current_frame.push_stack(
				 * VerificationType.integer_type); no_control_flow = false;
				 * break; case i2f : current_frame.pop_stack(
				 * VerificationType.integer_type); current_frame.push_stack(
				 * VerificationType.float_type); no_control_flow = false; break;
				 * case i2d : current_frame.pop_stack(
				 * VerificationType.integer_type); current_frame.push_stack_2(
				 * VerificationType.double_type, VerificationType.double2_type);
				 * no_control_flow = false; break; case l2f :
				 * current_frame.pop_stack_2( VerificationType.long2_type,
				 * VerificationType.long_type); current_frame.push_stack(
				 * VerificationType.float_type); no_control_flow = false; break;
				 * case l2d : current_frame.pop_stack_2(
				 * VerificationType.long2_type, VerificationType.long_type);
				 * current_frame.push_stack_2( VerificationType.double_type,
				 * VerificationType.double2_type); no_control_flow = false;
				 * break; case f2i : current_frame.pop_stack(
				 * VerificationType.float_type); current_frame.push_stack(
				 * VerificationType.integer_type); no_control_flow = false;
				 * break; case f2l : current_frame.pop_stack(
				 * VerificationType.float_type); current_frame.push_stack_2(
				 * VerificationType.long_type, VerificationType.long2_type);
				 * no_control_flow = false; break; case f2d :
				 * current_frame.pop_stack( VerificationType.float_type);
				 * current_frame.push_stack_2( VerificationType.double_type,
				 * VerificationType.double2_type); no_control_flow = false;
				 * break; case d2i : current_frame.pop_stack_2(
				 * VerificationType.double2_type, VerificationType.double_type);
				 * current_frame.push_stack( VerificationType.integer_type);
				 * no_control_flow = false; break; case d2l :
				 * current_frame.pop_stack_2( VerificationType.double2_type,
				 * VerificationType.double_type); current_frame.push_stack_2(
				 * VerificationType.long_type, VerificationType.long2_type);
				 * no_control_flow = false; break; case d2f :
				 * current_frame.pop_stack_2( VerificationType.double2_type,
				 * VerificationType.double_type); current_frame.push_stack(
				 * VerificationType.float_type); no_control_flow = false; break;
				 * case i2b : case i2c : case i2s : current_frame.pop_stack(
				 * VerificationType.integer_type); current_frame.push_stack(
				 * VerificationType.integer_type); no_control_flow = false;
				 * break; case lcmp : current_frame.pop_stack_2(
				 * VerificationType.long2_type, VerificationType.long_type);
				 * current_frame.pop_stack_2( VerificationType.long2_type,
				 * VerificationType.long_type); current_frame.push_stack(
				 * VerificationType.integer_type); no_control_flow = false;
				 * break; case fcmpl : case fcmpg : current_frame.pop_stack(
				 * VerificationType.float_type); current_frame.pop_stack(
				 * VerificationType.float_type); current_frame.push_stack(
				 * VerificationType.integer_type); no_control_flow = false;
				 * break; case dcmpl : case dcmpg : current_frame.pop_stack_2(
				 * VerificationType.double2_type, VerificationType.double_type);
				 * current_frame.pop_stack_2( VerificationType.double2_type,
				 * VerificationType.double_type); current_frame.push_stack(
				 * VerificationType.integer_type); no_control_flow = false;
				 * break; case if_icmpeq: case if_icmpne: case if_icmplt: case
				 * if_icmpge: case if_icmpgt: case if_icmple:
				 * current_frame.pop_stack( VerificationType.integer_type); //
				 * fall through case ifeq: case ifne: case iflt: case ifge: case
				 * ifgt: case ifle: current_frame.pop_stack(
				 * VerificationType.integer_type); target = bcs.dest();
				 * stackmap_table.check_jump_target( &current_frame, target);
				 * no_control_flow = false; break; */
				 case IF_ACMPEQ : 
				 case IF_ACMPNE : 
					 current_frame.pop_stack(VerificationType.reference_check()); 
					 // fall through 
				 case IFNULL: 
				 case IFNONNULL: 
					 current_frame.pop_stack(VerificationType.reference_check()); 
					 target = bcs.dest();
					 stackmap_table.check_jump_target (current_frame, target);
					 no_control_flow = false; 
					 break; 
				/* case goto : target =
				 * bcs.dest(); stackmap_table.check_jump_target( &current_frame,
				 * target); no_control_flow = true; break; case goto_w : target
				 * = bcs.dest_w(); stackmap_table.check_jump_target(
				 * &current_frame, target); no_control_flow = true; break; case
				 * tableswitch : case lookupswitch : verify_switch( &bcs,
				 * code_length, code_data, &current_frame, &stackmap_table);
				 * no_control_flow = true; break; case ireturn : type =
				 * current_frame.pop_stack( VerificationType.integer_type);
				 * verify_return_value(return_type, type, bci, &current_frame);
				 * no_control_flow = true; break; case lreturn : type2 =
				 * current_frame.pop_stack( VerificationType.long2_type); type =
				 * current_frame.pop_stack( VerificationType.long_type);
				 * verify_return_value(return_type, type, bci, &current_frame);
				 * no_control_flow = true; break; case freturn : type =
				 * current_frame.pop_stack( ); verify_return_value(return_type,
				 * type, bci, &current_frame); no_control_flow = true; break;
				 * case dreturn : type2 = current_frame.pop_stack(
				 * VerificationType.double2_type, CHECK_VERIFY(this)); type =
				 * current_frame.pop_stack( VerificationType.double_type);
				 * verify_return_value(return_type, type, bci, &current_frame);
				 * no_control_flow = true; break; case areturn : type =
				 * current_frame.pop_stack(
				 * VerificationType::reference_check());
				 * verify_return_value(return_type, type, bci, &current_frame);
				 * no_control_flow = true; break; case return : if (return_type
				 * != VerificationType::bogus_type()) {
				 * verify_error(ErrorContext::bad_code(bci),
				 * "Method expects a return value"); return; } // Make sure
				 * "this" has been initialized if current method is an // <init>
				 * if (_method->name() == vmSymbols::object_initializer_name()
				 * && current_frame.flag_this_uninit()) {
				 * verify_error(ErrorContext::bad_code(bci),
				 * "Constructor must call super() or this() " "before return");
				 * return; } no_control_flow = true; break; case getstatic :
				 * case putstatic : case getfield : case putfield :
				 * verify_field_instructions( &bcs, &current_frame, cp);
				 * no_control_flow = false; break;
				 */
				case INVOKEVIRTUAL:
				case INVOKESPECIAL:
				case INVOKESTATIC:
					verify_invoke_instructions(opcode, bcs, code_length, current_frame, (bci >= ex_min && bci < ex_max), this_uninit, return_type, stackmap_table);
					no_control_flow = false;
					break;
				/*
				 * case invokeinterface : case invokedynamic :
				 * verify_invoke_instructions( &bcs, code_length,
				 * &current_frame, (bci >= ex_min && bci < ex_max),
				 * &this_uninit, return_type, cp, &stackmap_table);
				 * no_control_flow = false; break;
				 */
				case NEW: {
					index = bcs.get_index_u2();
					verify_cp_class_type(bci, index);
					VerificationType new_class_type = cp_index_to_type(index);
					if (!new_class_type.is_object()) {
						throw new VerifierException("Illegal new instruction");
					}
					type = VerificationType.uninitialized_type((short) bci);
					current_frame.push_stack(type);
					no_control_flow = false;
					break;
				}
				/*
				 * case newarray : type = get_newarray_type(bcs.get_index(),
				 * bci); current_frame.pop_stack( VerificationType.integer_type,
				 * CHECK_VERIFY(this)); current_frame.push_stack(type);
				 * no_control_flow = false; break; case anewarray :
				 * verify_anewarray( bci, bcs.get_index_u2(), cp,
				 * &current_frame); no_control_flow = false; break; case
				 * arraylength : type = current_frame.pop_stack(
				 * VerificationType::reference_check()); if (!(type.is_null() ||
				 * type.is_array())) { verify_error(ErrorContext::bad_type( bci,
				 * current_frame.stack_top_ctx()), bad_type_msg, "arraylength");
				 * } current_frame.push_stack( VerificationType.integer_type);
				 * no_control_flow = false; break; case checkcast : { index =
				 * bcs.get_index_u2(); verify_cp_class_type(bci, index, cp);
				 * current_frame.pop_stack(object_type()); VerificationType
				 * klass_type = cp_index_to_type( index, cp);
				 * current_frame.push_stack(klass_type); no_control_flow =
				 * false; break; } case instanceof : { index =
				 * bcs.get_index_u2(); verify_cp_class_type(bci, index, cp);
				 * current_frame.pop_stack(object_type());
				 * current_frame.push_stack( VerificationType.integer_type);
				 * no_control_flow = false; break; } case monitorenter : case
				 * monitorexit : current_frame.pop_stack(
				 * VerificationType::reference_check()); no_control_flow =
				 * false; break; case multianewarray : { index =
				 * bcs.get_index_u2(); u2 dim = *(bcs.bcp()+3);
				 * verify_cp_class_type(bci, index, cp); VerificationType
				 * new_array_type = cp_index_to_type(index, cp); if
				 * (!new_array_type.is_array()) {
				 * verify_error(ErrorContext::bad_type(bci,
				 * TypeOrigin::cp(index, new_array_type)),
				 * "Illegal constant pool index in multianewarray instruction");
				 * return; } if (dim < 1 || new_array_type.dimensions() < dim) {
				 * verify_error(ErrorContext::bad_code(bci),
				 * "Illegal dimension in multianewarray instruction: %d", dim);
				 * return; } for (int i = 0; i < dim; i++) {
				 * current_frame.pop_stack( VerificationType.integer_type); }
				 * current_frame.push_stack(new_array_type); no_control_flow =
				 * false; break; } case athrow : type =
				 * VerificationType::reference_type(
				 * vmSymbols::java_lang_Throwable());
				 * current_frame.pop_stack(type); no_control_flow = true; break;
				 * default: // We only need to check the valid bytecodes in
				 * class file. // And jsr and ret are not in the new class file
				 * format in JDK1.5. verify_error(ErrorContext::bad_code(bci),
				 * "Bad instruction: %02x", opcode); no_control_flow = false;
				 * return;
				 */
				default:
					throw new UnsupportedOperationException(opcode.name());
				} // end switch
			} // end Merge with the next instruction

			// Look for possible jump target in exception handlers and see if it
			// matches current_frame
			if (bci >= ex_min && bci < ex_max) {
				verify_exception_handler_targets(bci, this_uninit, current_frame, stackmap_table);
			}
		} // end while

		// Make sure that control flow does not fall through end of the method
		if (!no_control_flow) {
			throw new VerifierException("Control flow falls through code end");
		}
	}

	private void verify_aload(short index, StackMapFrame current_frame) {
		VerificationType type = current_frame.get_local(index, VerificationType.reference_check());
		current_frame.push_stack(type);
	}

	private void verify_astore(short index, StackMapFrame current_frame) {
		VerificationType type = current_frame.pop_stack(VerificationType.reference_check());
		current_frame.set_local(index, type);
	}

	private VerificationType cp_index_to_type(short index) {
		IConstantOperand operand = constantWithIndex(index);
		if (operand instanceof ClassConstant)
			return VerificationType.reference_type(((ClassConstant) operand).getClassName().getValue());
		else
			throw new UnsupportedOperationException(operand.getClass().getName()); // for
																					// now
	}

	private void verify_cp_class_type(int bci, short index) {
		verify_cp_index(bci, index);
		IConstantOperand operand = constantWithIndex(index);
		if (!(operand instanceof ClassConstant))
			throw new VerifierException("Illegal type at constant pool entry " + index);
	}

	private void verify_cp_index(int bci, short index) {
		IConstantOperand operand = constantWithIndex(index);
		if (operand == null)
			throw new VerifierException("Illegal type at constant pool entry " + index);
	}

	private void verify_exception_handler_targets(int bci, AtomicInteger this_uninit, StackMapFrame current_frame, StackMapTable stackmap_table) {
		throw new UnsupportedOperationException();
	}

	private int verify_stackmap_table(int stackmap_index, int bci, StackMapFrame current_frame, StackMapTable stackmap_table, boolean no_control_flow) {

		if (stackmap_index < stackmap_table.get_frame_count()) {
			int this_offset = stackmap_table.get_offset(stackmap_index);
			if (no_control_flow && this_offset > bci) {
				throw new VerifierException("Expecting a stack map frame");
			}
			if (this_offset == bci) {
				// See if current stack map can be assigned to the frame in table.
				// current_frame is the stackmap frame got from the last instruction.
				// If matched, current_frame will be updated by this method.
				stackmap_table.match_stackmap(current_frame, this_offset, stackmap_index, !no_control_flow, true, false);
				stackmap_index++;
			} else if (this_offset < bci) {
				// current_offset should have met this_offset.
				throw new VerifierException("Bad stack map offset " + this_offset);
			}
		} else if (no_control_flow) {
			throw new VerifierException("Expecting a stack map frame");
		}
		return stackmap_index;
	}

	private byte[] generate_code_data(MethodInfo m, int code_length) {
		byte[] code_data = new byte[code_length];
		RawBytecodeStream bcs = new RawBytecodeStream(m.getCodeAttribute().getOpcodes());
		while (!bcs.is_last_bytecode()) {
			Opcode opcode = bcs.raw_next();
			if (opcode == Opcode.ILLEGAL)
				throw new VerifierException("Illegal opcode");
			int bci = bcs.bci();
			if (opcode == Opcode.NEW)
				code_data[bci] = NEW_OFFSET;
			else
				code_data[bci] = BYTECODE_OFFSET;
		}
		return code_data;
	}

	public int change_sig_to_verificationType(SignatureStream sig_type, VerificationType[] inference_types, int index) {
		BasicType bt = sig_type.type();
		switch (bt) {
		case T_OBJECT:
		case T_ARRAY:
			inference_types[index] = VerificationType.reference_type(sig_type.as_symbol());
			return 1;
		case T_LONG:
			inference_types[index] = VerificationType.long_type;
			inference_types[index + 1] = VerificationType.long2_type;
			return 2;
		case T_DOUBLE:
			inference_types[index] = VerificationType.double_type;
			inference_types[index + 1] = VerificationType.double2_type;
			return 2;
		case T_INT:
		case T_BOOLEAN:
		case T_BYTE:
		case T_CHAR:
		case T_SHORT:
			inference_types[index] = VerificationType.integer_type;
			return 1;
		case T_FLOAT:
			inference_types[index] = VerificationType.float_type;
			return 1;
		default:
			throw new UnsupportedOperationException();
		}
	}

	void verify_invoke_instructions(Opcode opcode, RawBytecodeStream bcs, int code_length, StackMapFrame current_frame, boolean in_try_block, AtomicInteger this_uninit, VerificationType return_type,
			StackMapTable stackmap_table) {
		// Make sure the constant pool item is the right type
		short index = bcs.get_index_u2();
		int types;
		switch (opcode) {
		case INVOKEINTERFACE:
			types = 1 << Tags.CONSTANT_InterfaceMethodref;
			break;
		case INVOKEDYNAMIC:
			types = 1 << Tags.CONSTANT_InvokeDynamic;
			break;
		case INVOKESPECIAL:
		case INVOKESTATIC:
			types = (1 << Tags.CONSTANT_InterfaceMethodref) | (1 << Tags.CONSTANT_Methodref);
			break;
		default:
			types = 1 << Tags.CONSTANT_Methodref;
		}
		verify_cp_type(bcs.bci(), index, types);

		// Get method name and signature
		IOperand operand = constantWithIndex(index);
		String method_name = ((MethodConstant) operand).getMethodNameAndType().getName().getValue();
		String method_sig = ((MethodConstant) operand).getMethodNameAndType().getDescriptor().toString();

		if (!SignatureVerifier.is_valid_method_signature(method_sig)) {
			throw new VerifierException("Invalid method signature: " + method_sig);
		}

		// Get referenced class type
		VerificationType ref_class_type = cp_ref_index_to_type(index);

		int size = ArgumentSizeComputer.size(method_sig);
		VerificationType[] sig_types = new VerificationType[size];
		SignatureStream sig_stream = new SignatureStream(method_sig);
		int sig_i = 0;
		while (!sig_stream.at_return_type()) {
			sig_i += change_sig_to_verificationType(sig_stream, sig_types, sig_i);
			sig_stream.next();
		}
		int nargs = sig_i;

		// Check instruction operands
		int bci = bcs.bci();
		if (opcode == Opcode.INVOKEINTERFACE) {

			// 4905268: count operand in invokeinterface should be nargs+1, not nargs.
			// JSR202 spec: The count operand of an invokeinterface instruction is valid if it is
			// the difference between the size of the operand stack before and after the instruction
			// executes.
			if (bcs.get_index_u1_at(bci + 3) != (nargs + 1)) {
				throw new VerifierException("Inconsistent args count operand in invokeinterface");
			}
			if (bcs.get_index_u1_at(bci + 4) != 0) {
				throw new VerifierException("Fourth operand byte of invokeinterface must be zero");
			}
		}

		if (opcode == Opcode.INVOKEDYNAMIC) {
			if (bcs.get_index_u1_at(bci + 3) != 0 || bcs.get_index_u1_at(bci + 4) != 0) {
				throw new VerifierException("Third and fourth operand bytes of invokedynamic must be zero");
			}
		}

		if (method_name.charAt(0) == '<') {
			// Make sure <init> can only be invoked by invokespecial
			if (opcode != Opcode.INVOKESPECIAL || !"<init>".equals(method_name)) {
				throw new VerifierException("Illegal call to internal method");
			}
		} else if (opcode == Opcode.INVOKESPECIAL && !is_same_or_direct_interface(current_class(), current_type(), ref_class_type)
				&& !ref_class_type.equals(VerificationType.reference_type(_klass.superclass_name()))) {
			boolean subtype = false;
			operand = constantWithIndex(index);
			boolean have_imr_indirect = operand instanceof InterfaceConstant;
			if (!current_class().is_anonymous()) {
				subtype = ref_class_type.is_assignable_from(current_type(), false, this);
			} else {
				VerificationType host_klass_type = VerificationType.reference_type(current_class().host_klass().name());
				subtype = ref_class_type.is_assignable_from(host_klass_type, false, this);

				// If invokespecial of IMR, need to recheck for same or
				// direct interface relative to the host class
				have_imr_indirect = false; 
				/*
				 * (have_imr_indirect &&
				 * !is_same_or_direct_interface(
				 * InstanceKlass
				 * ::cast(current_class(
				 * )->host_klass()),
				 * host_klass_type,
				 * ref_class_type));
				 */
			}
			if (!subtype) {
				throw new VerifierException("Bad invokespecial instruction: current class isn't assignable to reference class.");
			} else if (have_imr_indirect) {
				throw new VerifierException("Bad invokespecial instruction: interface method reference is in an indirect superinterface.");
			}

		}
		// Match method descriptor with operand stack
		for (int i = nargs - 1; i >= 0; i--) { // Run backwards
			current_frame.pop_stack(sig_types[i]);
		}
		// Check objectref on operand stack
		if (opcode != Opcode.INVOKESTATIC && opcode != Opcode.INVOKEDYNAMIC) {
			if ("<init>".equals(method_name)) { // <init> method
				verify_invoke_init(bcs, index, ref_class_type, current_frame, code_length, in_try_block, this_uninit, stackmap_table);
			} else { // other methods
				// Ensures that target class is assignable to method class.
				if (opcode == Opcode.INVOKESPECIAL) {
					if (!current_class().is_anonymous()) {
						current_frame.pop_stack(current_type());
					} else {
						// anonymous class invokespecial calls: check if the
						// objectref is a subtype of the host_klass of the
						// current class
						// to allow an anonymous class to reference methods in
						// the host_klass
						VerificationType top = current_frame.pop_stack();
						VerificationType hosttype = VerificationType.reference_type(current_class().host_klass().name());
						boolean subtype = hosttype.is_assignable_from(top, false, this);
						if (!subtype) {
							throw new VerifierException("Bad type on operand stack");
						}
					}
				} else if (opcode == Opcode.INVOKEVIRTUAL) { /*
															 * VerificationType
															 * stack_object_type
															 * = current_frame.
															 * pop_stack
															 * (ref_class_type);
															 * if
															 * (current_type()
															 * !=
															 * stack_object_type
															 * ) { operand =
															 * classFile
															 * .getConstantsPool
															 * (
															 * ).constantWithIndex
															 * (index); String
															 * ref_class_name =
															 * ((ClassConstant)
															 * operand
															 * ).getClassName
															 * ().getValue(); //
															 * See the comments
															 * in
															 * verify_field_instructions
															 * () for // the
															 * rationale behind
															 * this. if
															 * (name_in_supers
															 * (ref_class_name,
															 * current_class()))
															 * { Klass*
															 * ref_class =
															 * load_class
															 * (ref_class_name,
															 * CHECK); if
															 * (is_protected_access
															 * ( _klass,
															 * ref_class,
															 * method_name,
															 * method_sig,
															 * true)) { // It's
															 * protected access,
															 * check if stack
															 * object is //
															 * assignable to
															 * current class.
															 * bool
															 * is_assignable =
															 * current_type
															 * ().is_assignable_from
															 * (
															 * stack_object_type
															 * , this, true,
															 * CHECK_VERIFY
															 * (this)); if
															 * (!is_assignable)
															 * { if
															 * (ref_class_type
															 * .name() ==
															 * vmSymbols
															 * ::java_lang_Object
															 * () &&
															 * stack_object_type
															 * .is_array() &&
															 * method_name ==
															 * vmSymbols
															 * ::clone_name()) {
															 * // Special case:
															 * arrays pretend to
															 * implement public
															 * Object //
															 * clone(). } else {
															 * verify_error
															 * (ErrorContext
															 * ::bad_type(bci,
															 * current_frame
															 * ->stack_top_ctx
															 * (),
															 * TypeOrigin::implicit
															 * (
															 * current_type())),
															 * "Bad access to protected data in invokevirtual"
															 * ); return; } } }
															 * } }
															 */
				} else {
					if (opcode != Opcode.INVOKEINTERFACE)
						throw new VerifierException("Unexpected opcode encountered");
					current_frame.pop_stack(ref_class_type);
				}
			}
		}
		// Push the result type.
		if (sig_stream.type() != BasicType.T_VOID) {
			if ("<init>".equals(method_name)) {
				// <init> method must have a void return type
				/*
				 * Unreachable? Class file parser verifies that methods with '<'
				 * have void return
				 */
				throw new VerifierException("Return type must be void in <init> method");
			}
			VerificationType[] return_types = new VerificationType[2];
			int n = change_sig_to_verificationType(sig_stream, return_types, 0);
			for (int i = 0; i < n; i++) {
				current_frame.push_stack(return_types[i]); // push types
															// backwards
			}
		}
	}

	private boolean is_same_or_direct_interface(Klass current_class, VerificationType current_type, VerificationType ref_class_type) {
		throw new UnsupportedOperationException();
	}

	public Klass current_class() {
		return _klass;
	}

	private void verify_invoke_init(RawBytecodeStream bcs, int ref_class_index, VerificationType ref_class_type, StackMapFrame current_frame, int code_length, boolean in_try_block,
			AtomicInteger this_uninit, StackMapTable stackmap_table) {
		int bci = bcs.bci();
		VerificationType type = current_frame.pop_stack(VerificationType.reference_check());
		if (type == VerificationType.uninitialized_this_type) {
			/*
			 * // The method must be an <init> method of this class or its
			 * superclass Klass superk = current_class().superclass(); if
			 * (ref_class_type.name() != current_class().name() &&
			 * ref_class_type.name() != superk->name()) { throw new
			 * VerifierException("Bad <init> method call"); }
			 * 
			 * // If this invokespecial call is done from inside of a TRY block
			 * then make // sure that all catch clause paths end in a throw.
			 * Otherwise, this can // result in returning an incomplete object.
			 * if (in_try_block) { ExceptionTable exhandlers(_method()); int
			 * exlength = exhandlers.length(); for(int i = 0; i < exlength; i++)
			 * { u2 start_pc = exhandlers.start_pc(i); u2 end_pc =
			 * exhandlers.end_pc(i);
			 * 
			 * if (bci >= start_pc && bci < end_pc) { if
			 * (!ends_in_athrow(exhandlers.handler_pc(i))) {
			 * verify_error(ErrorContext::bad_code(bci),
			 * "Bad <init> method call from after the start of a try block");
			 * return; } else if (VerboseVerification) { ResourceMark rm;
			 * tty->print_cr( "Survived call to ends_in_athrow(): %s",
			 * current_class()->name()->as_C_string()); } } }
			 * 
			 * // Check the exception handler target stackmaps with the locals
			 * from the // incoming stackmap (before initialize_object() changes
			 * them to outgoing // state). verify_exception_handler_targets(bci,
			 * true, current_frame, stackmap_table, CHECK_VERIFY(this)); } //
			 * in_try_block
			 */
			current_frame.initialize_object(type, current_type());
			this_uninit.set(1);
		} else if (type.is_uninitialized()) {
			byte[] code_data = bcs.opcodes;
			short new_offset = type.bci();
			int new_bci = bcs.bci() - bci + new_offset;
			if (new_offset > (code_length - 3) || ((code_data[new_bci] & 0xFF) != Opcode.NEW.opcode)) {
				// Unreachable? Stack map parsing ensures valid type and new
				// instructions have a valid BCI.
				throw new VerifierException("Expecting new instruction");
			}
			short new_class_index = bcs.get_index_u2_at(new_bci + 1);
			verify_cp_class_type(bci, new_class_index);

			// The method must be an <init> method of the indicated class
			VerificationType new_class_type = cp_index_to_type(new_class_index);
			if (!new_class_type.equals(ref_class_type)) {
				throw new VerifierException("Call to wrong <init> method");
			}
			/*
			 * // According to the VM spec, if the referent class is a
			 * superclass of the // current class, and is in a different runtime
			 * package, and the method is // protected, then the objectref must
			 * be the current class or a subclass // of the current class.
			 * VerificationType objectref_type = new_class_type; if
			 * (name_in_supers(ref_class_type.name(), current_class())) { Klass*
			 * ref_klass = load_class( ref_class_type.name(),
			 * CHECK_VERIFY(this)); Method* m =
			 * InstanceKlass::cast(ref_klass)->uncached_lookup_method(
			 * vmSymbols::object_initializer_name(),
			 * cp->signature_ref_at(bcs->get_index_u2()), Klass::normal); // Do
			 * nothing if method is not found. Let resolution detect the error.
			 * if (m != NULL) { instanceKlassHandle mh(THREAD,
			 * m->method_holder()); if (m->is_protected() &&
			 * !mh->is_same_class_package(_klass())) { bool assignable =
			 * current_type().is_assignable_from( objectref_type, this, true,
			 * CHECK_VERIFY(this)); if (!assignable) {
			 * verify_error(ErrorContext::bad_type(bci,
			 * TypeOrigin::cp(new_class_index, objectref_type),
			 * TypeOrigin::implicit(current_type())),
			 * "Bad access to protected <init> method"); return; } } } }
			 */
			// Check the exception handler target stackmaps with the locals from
			// the
			// incoming stackmap (before initialize_object() changes them to
			// outgoing
			// state).
			if (in_try_block) {
				verify_exception_handler_targets(bci, this_uninit, current_frame, stackmap_table);
			}
			current_frame.initialize_object(type, new_class_type);
		} else {
			throw new VerifierException("Bad operand type when invoking <init>");
		}
	}

	private VerificationType cp_ref_index_to_type(short index) {
		IConstantOperand operand = constantWithIndex(index);
		if (operand instanceof FieldConstant)
			return cp_index_to_type((short) ((FieldConstant) operand).getClassName().getIndexInConstantPool());
		else if (operand instanceof MethodConstant) // or InterfaceConstant
			return cp_index_to_type((short) ((MethodConstant) operand).getClassName().getIndexInConstantPool());
		else
			throw new VerifierException("Corrupted constant pool");
	}

	private void verify_cp_type(int bci, short index, int types) {
		verify_cp_index(bci, index);
		int tag = _klass.tag_at(index);
		if ((types & (1 << tag)) == 0) {
			throw new VerifierException("Illegal type at constant pool entry " + index);
		}
	}

	public IConstantOperand constantWithIndex(int index) {
		return _klass.constantWithIndex(index);
	}

}
