package prompto.verifier;

import prompto.compiler.IVerifierEntry;
import prompto.compiler.IVerifierEntry.VerifierType;

public class VerificationType {

	static final int BitsPerByte = 8;

	// Bottom two bits determine if the type is a reference, primitive,
	// uninitialized or a query-type.
	static final int TypeMask = 0x00000003;

	// Topmost types encoding
	static final int Reference = 0x0; // _sym contains the name
	static final int Primitive = 0x1; // see below for primitive list
	static final int Uninitialized = 0x2; // 0x00ffff00 contains bci
	static final int TypeQuery = 0x3; // Meta-types used for category testing

	// Utility flags
	static final int ReferenceFlag = 0x00; // For reference query types
	static final int Category1Flag = 0x01; // One-word values
	static final int Category2Flag = 0x02; // First word of a two-word value
	static final int Category2_2ndFlag = 0x04; // Second word of a two-word
												// value

	// special reference values
	static final int Null = 0x00000000; // A reference with a 0 sym is null

	// Primitives categories (the second byte determines the category)
	static final int Category1 = (Category1Flag << 1 * BitsPerByte) | Primitive;
	static final int Category2 = (Category2Flag << 1 * BitsPerByte) | Primitive;
	static final int Category2_2nd = (Category2_2ndFlag << 1 * BitsPerByte) | Primitive;

	static final int ITEM_Bogus = -1;

	static final int ITEM_Top = IVerifierEntry.VerifierType.ITEM_Top.ordinal();
	static final int ITEM_Integer = IVerifierEntry.VerifierType.ITEM_Integer.ordinal();
	static final int ITEM_Float = IVerifierEntry.VerifierType.ITEM_Float.ordinal();
	static final int ITEM_Double = IVerifierEntry.VerifierType.ITEM_Double.ordinal();
	static final int ITEM_Long = IVerifierEntry.VerifierType.ITEM_Long.ordinal();
	static final int ITEM_Null = IVerifierEntry.VerifierType.ITEM_Null.ordinal();
	static final int ITEM_UninitializedThis = IVerifierEntry.VerifierType.ITEM_UninitializedThis.ordinal();
	static final int ITEM_Object = IVerifierEntry.VerifierType.ITEM_Object.ordinal();
	static final int ITEM_Uninitialized = IVerifierEntry.VerifierType.ITEM_Uninitialized.ordinal();

	static final int ITEM_Boolean = 9;
	static final int ITEM_Byte = 10;
	static final int ITEM_Short = 11;
	static final int ITEM_Char = 12;
	static final int ITEM_Long_2nd = 13;
	static final int ITEM_Double_2nd = 14;

	// Primitive values (type descriminator stored in most-signifcant bytes)
	static final int Bogus = (ITEM_Bogus << 2 * BitsPerByte) | Category1;
	static final int Boolean = (ITEM_Boolean << 2 * BitsPerByte) | Category1;
	static final int Byte = (ITEM_Byte << 2 * BitsPerByte) | Category1;
	static final int Short = (ITEM_Short << 2 * BitsPerByte) | Category1;
	static final int Char = (ITEM_Char << 2 * BitsPerByte) | Category1;
	static final int Integer = (ITEM_Integer << 2 * BitsPerByte) | Category1;
	static final int Float = (ITEM_Float << 2 * BitsPerByte) | Category1;
	static final int Long = (ITEM_Long << 2 * BitsPerByte) | Category2;
	static final int Double = (ITEM_Double << 2 * BitsPerByte) | Category2;
	static final int Long_2nd = (ITEM_Long_2nd << 2 * BitsPerByte) | Category2_2nd;
	static final int Double_2nd = (ITEM_Double_2nd << 2 * BitsPerByte) | Category2_2nd;

	// Used by Uninitialized (second and third bytes hold the bci)
	static final int BciMask = 0xffff << 1 * BitsPerByte;
	static final short BciForThis = (short) -1; // A bci of -1 is an
												// Uninitialized-This

	// Query values
	static final int ReferenceQuery = (ReferenceFlag << 1 * BitsPerByte) | TypeQuery;
	static final int Category1Query = (Category1Flag << 1 * BitsPerByte) | TypeQuery;
	static final int Category2Query = (Category2Flag << 1 * BitsPerByte) | TypeQuery;
	static final int Category2_2ndQuery = (Category2_2ndFlag << 1 * BitsPerByte) | TypeQuery;

	static final VerificationType bogus_type = new VerificationType(Bogus);
	static final VerificationType top_type = bogus_type;
	static final VerificationType null_type = new VerificationType(Null);
	static final VerificationType boolean_type = new VerificationType(Boolean);
	static final VerificationType char_type = new VerificationType(Char);
	static final VerificationType byte_type = new VerificationType(Byte);
	static final VerificationType short_type = new VerificationType(Short);
	static final VerificationType integer_type = new VerificationType(Integer);
	static final VerificationType long_type = new VerificationType(Long);
	static final VerificationType long2_type = new VerificationType(Long_2nd);
	static final VerificationType float_type = new VerificationType(Float);
	static final VerificationType double_type = new VerificationType(Double);
	static final VerificationType double2_type = new VerificationType(Double_2nd);

	static VerificationType reference_type(String name) {
		return new VerificationType(name);
	}

	static VerificationType uninitialized_type(short bci) {
		return new VerificationType(bci << 1 * BitsPerByte | Uninitialized);
	}

	static final VerificationType uninitialized_this_type = uninitialized_type(BciForThis);

	public static VerificationType from_tag(int tag) {
		if (tag == ITEM_Top)
			return bogus_type;
		else if (tag == ITEM_Integer)
			return integer_type;
		else if (tag == ITEM_Float)
			return float_type;
		else if (tag == ITEM_Double)
			return double_type;
		else if (tag == ITEM_Long)
			return long_type;
		else if (tag == ITEM_Null)
			return null_type;
		else
			throw new VerifierException("Should never get there!");
	}

	// "check" types are used for queries. A "check" type is not assignable
	// to anything, but the specified types are assignable to a "check". For
	// example, any category1 primitive is assignable to category1_check and
	// any reference is assignable to reference_check.
	static VerificationType reference_check() {
		return new VerificationType(ReferenceQuery);
	}

	static VerificationType category1_check() {
		return new VerificationType(Category1Query);
	}

	static VerificationType category2_check() {
		return new VerificationType(Category2Query);
	}

	static VerificationType category2_2nd_check() {
		return new VerificationType(Category2_2ndQuery);
	}

	public VerificationType to_category2_2nd() {
		throw new UnsupportedOperationException();
	}

	public boolean is_uninitialized_this() {
		throw new UnsupportedOperationException();
	}

	int _data;
	String _name;

	public VerificationType(int data) {
		this._data = data;
	}

	public VerificationType(String name) {
		this._data = ~0x3;
		this._name = name;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof VerificationType
			&& equals((VerificationType)obj);
	}
	
	@Override
	public String toString() {
		if(_name!=null)
			return _name;
		else if(is_bogus())
			return "bogus";
		else {
			int data = (_data >> 16) & 0xFFFF;
			VerifierType type = VerifierType.values()[data];
			return type.name();
		}
	}
	
	public boolean equals(VerificationType t) {
		return is_reference() ?
				(t.is_reference() && !is_null() && !t.is_null() && name() == t.name()) :
				_data == t._data;
	}
	
	public String name() {
		return _name;
	}

	boolean is_category1() {
		// This should return true for all one-word types, which are category1
		// primitives, and references (including uninitialized refs). Though
		// the 'query' types should technically return 'false' here, if we
		// allow this to return true, we can perform the test using only
		// 2 operations rather than 8 (3 masks, 3 compares and 2 logical
		// 'ands').
		// Since noone should call this on a query type anyway, this is ok.
		if (is_check())
			throw new VerifierException("Must not be a check type (wrong value returned)");
		return (_data & Category1) != Primitive;
		// should only return false if it's a primitive, and the category1 flag
		// is not set.
	}

	public boolean is_category2() {
		return (_data & Category2) == Category2;
	}

	boolean is_category2_2nd() {
		return (_data & Category2_2nd) == Category2_2nd;
	}

	public boolean is_object() {
		return (is_reference() && !is_null() && name().length() >= 1 && name().charAt(0) != '[');
	}

	boolean is_array() {
		return (is_reference() && !is_null() && name().length() >= 1 && name().charAt(0) == '[');
	}

	boolean is_integer() {
		return _data == Integer;
	}

	public boolean is_long() {
		return _data == Long;
	}

	public boolean is_long2() {
		return _data == Long_2nd;
	}

	public boolean is_double() {
		return _data == Double;
	}

	public boolean is_double2() {
		return _data == Double_2nd;
	}
	
	private boolean is_null() {
		return _data == Null;
	}

	private boolean is_reference() {
		return (_data & TypeMask) == Reference;
	}

	public boolean is_check() {
		return (_data & TypeQuery) == TypeQuery;
	}

	private boolean is_bogus() {
		return _data == Bogus;
	}

	boolean is_uninitialized() {
		return (_data & Uninitialized) == Uninitialized;
	}

	VerificationType get_component(ClassVerifier verifier) {
		if (!is_array() || name().length() < 2)
			throw new VerifierException("Must be a valid array");
		String component;
		switch (name().charAt(1)) {
		case 'Z':
			return new VerificationType(Boolean);
		case 'B':
			return new VerificationType(Byte);
		case 'C':
			return new VerificationType(Char);
		case 'S':
			return new VerificationType(Short);
		case 'I':
			return new VerificationType(Integer);
		case 'J':
			return new VerificationType(Long);
		case 'F':
			return new VerificationType(Float);
		case 'D':
			return new VerificationType(Double);
		case '[':
			component = name().substring(1);
			return VerificationType.reference_type(component);
		case 'L':
			component = name().substring(1, name().length() - 1);
			return VerificationType.reference_type(component);
		default:
			// Met an invalid type signature, e.g. [X
			throw new VerifierException("Invalid signature");
		}
	}

	public boolean is_assignable_from(VerificationType from, boolean from_field_is_protected, ClassVerifier verifier) {
		// The whole point of this type system - check to see if one type
		// is assignable to another. Returns true if one can assign 'from' to
		// this.
		if (equals(from) || is_bogus()) {
			return true;
		} else {
			switch (_data) {
			case Category1Query:
				return from.is_category1();
			case Category2Query:
				return from.is_category2();
			case Category2_2ndQuery:
				return from.is_category2_2nd();
			case ReferenceQuery:
				return from.is_reference() || from.is_uninitialized();
			case Boolean:
			case Byte:
			case Char:
			case Short:
				// An int can be assigned to boolean, byte, char or short
				// values.
				return from.is_integer();
			default:
				if (is_reference() && from.is_reference()) {
					return is_reference_assignable_from(from, from_field_is_protected, verifier);
				} else {
					return false;
				}
			}
		}
	}

	boolean is_reference_assignable_from(VerificationType from, boolean from_field_is_protected, ClassVerifier verifier) {
		if (from.is_null()) {
			// null is assignable to any reference
			return true;
		} else if (is_null()) {
			return false;
		} else if (name() == from.name()) {
			return true;
		} else if (is_object()) {
			// We need check the class hierarchy to check assignability
			if ("java/lang/Object".equals(name())) {
				// any object or array is assignable to java.lang.Object
				return true;
			}
			if (verifier.current_class().is_interface() && (!from_field_is_protected || "java/lang/Object".equals(from.name()))) {
				// If we are not trying to access a protected field or method in
				// java.lang.Object then we treat interfaces as
				// java.lang.Object,
				// including java.lang.Cloneable and java.io.Serializable.
				return true;
			} else if (from.is_object()) {
				return true;
				// return
				// InstanceKlass::cast(from_class)->is_subclass_of(this_class());
			}
		} else if (is_array() && from.is_array()) {
			VerificationType comp_this = get_component(verifier);
			VerificationType comp_from = from.get_component(verifier);
			if (!comp_this.is_bogus() && !comp_from.is_bogus()) {
				return comp_this.is_assignable_from(comp_from, from_field_is_protected, verifier);
			}
		}
		return false;
	}

	public short bci() {
		if(!is_uninitialized())
			throw new VerifierException("Must be uninitialized type");
		return (short)((_data & BciMask) >> 1 * BitsPerByte);
	}




}
