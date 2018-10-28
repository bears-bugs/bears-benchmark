package prompto.type;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.parser.ISection;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.utils.CodeWriter;
import prompto.value.IValue;
import prompto.value.RangeBase;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class BaseType extends Section implements IType {

	Family family;

	protected BaseType(Family family) {
		this.family = family;
	}

	@Override
	public Family getFamily() {
		return family;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IType))
			return false;
		IType type = (IType) obj;
		return this.getTypeName().equals(type.getTypeName());
	}

	@Override
	public String toString() {
		return getTypeName();
	}
	
	@Override
	public String getTranspiledName(Context context) {
		return getTypeName();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(getTypeName());
	}
	
	@Override
	public IType checkAdd(Context context, IType other, boolean tryReverse) {
		if(other instanceof EnumeratedNativeType)
			return checkAdd(context, ((EnumeratedNativeType)other).getDerivedFrom(), tryReverse);
		else if(tryReverse)
			return other.checkAdd(context, this, false);
		else
			throw new SyntaxError("Cannot add " + this.getTypeName() + " to " + other.getTypeName());
	}

	@Override
	public IType checkSubstract(Context context, IType other) {
		if(other instanceof EnumeratedNativeType)
			return checkSubstract(context, ((EnumeratedNativeType)other).getDerivedFrom());
		else 		
			throw new SyntaxError("Cannot substract " + this.getTypeName() + " from " + other.getTypeName());
	}

	@Override
	public IType checkDivide(Context context, IType other) {
		if(other instanceof EnumeratedNativeType)
			return checkDivide(context, ((EnumeratedNativeType)other).getDerivedFrom());
		else 		
			throw new SyntaxError("Cannot divide " + this.getTypeName() + " with " + other.getTypeName());
	}

	@Override
	public IType checkIntDivide(Context context, IType other) {
		if(other instanceof EnumeratedNativeType)
			return checkIntDivide(context, ((EnumeratedNativeType)other).getDerivedFrom());
		else 		
			throw new SyntaxError("Cannot divide " + this.getTypeName() + " with " + other.getTypeName());
	}

	@Override
	public IType checkMultiply(Context context, IType other, boolean tryReverse) {
		if(other instanceof EnumeratedNativeType)
			return checkMultiply(context, ((EnumeratedNativeType)other).getDerivedFrom(), tryReverse);
		else if(tryReverse)
			return other.checkMultiply(context, this, false);
		else
			throw new SyntaxError("Cannot multiply " + this.getTypeName() + " with " + other.getTypeName());
	}

	@Override
	public IType checkModulo(Context context, IType other) {
		if(other instanceof EnumeratedNativeType)
			return checkModulo(context, ((EnumeratedNativeType)other).getDerivedFrom());
		else 		
			throw new SyntaxError("Cannot modulo " + this.getTypeName() + " with " + other.getTypeName());
	}
	
	@Override
	public IType checkCompare(Context context, IType other, ISection section) {
		if(other instanceof EnumeratedNativeType)
			return checkCompare(context, ((EnumeratedNativeType)other).getDerivedFrom(), section);
		else 		
			context.getProblemListener().reportIllegalComparison(this, other, section);
		return BooleanType.instance();
	}

	@Override
	public IType checkContains(Context context, IType other) {
		if(other instanceof EnumeratedNativeType)
			return checkContains(context, ((EnumeratedNativeType)other).getDerivedFrom());
		else 		
			throw new SyntaxError(this.getTypeName() + " cannot contain " + other.getTypeName());
	}

	@Override
	public IType checkContainsAllOrAny(Context context, IType other) {
		if(other instanceof EnumeratedNativeType)
			return checkContainsAllOrAny(context, ((EnumeratedNativeType)other).getDerivedFrom());
		else 		
			throw new SyntaxError(this.getTypeName() + " cannot contain " + other.getTypeName());
	}

	@Override
	public IType checkItem(Context context, IType itemType) {
		throw new SyntaxError("Cannot read item from " + this.getTypeName());
	}

	@Override
	public IType checkMember(Context context, Identifier name) {
		context.getProblemListener().reportIllegalMember(name.toString(), name);
		return VoidType.instance();
	}

	@Override
	public IType checkSlice(Context context) {
		throw new SyntaxError("Cannot slice " + this.getTypeName());
	}

	@Override
	public IType checkIterator(Context context) {
		throw new SyntaxError("Cannot iterate over " + this.getTypeName());
	}

	@Override
	public abstract void checkUnique(Context context);

	@Override
	public abstract void checkExists(Context context);

	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return this==other 
				|| this.equals(other) 
				|| other.equals(NullType.instance());
	}

	@Override
	public abstract boolean isMoreSpecificThan(Context context, IType other);

	@Override
	public final void checkAssignableFrom(Context context, IType other) {
		if (!isAssignableFrom(context, other))
			throw new SyntaxError("Type: " + other.getTypeName() + " is not compatible with: " + this.getTypeName());
	}

	@Override
	public IType checkRange(Context context, IType other) {
		throw new SyntaxError("Cannot create range of " + this.getTypeName() + " and " + other.getTypeName());
	}

	@Override
	public RangeBase<?> newRange(Object left, Object right) {
		throw new SyntaxError("Cannot create range of " + this.getTypeName());
	}

	@Override
	public String toString(Object value) {
		return value.toString();
	}

	@Override
	public Comparator<? extends IValue> getComparator(boolean descending) {
		throw new RuntimeException("Unsupported!");
	}

	public IValue convertJavaValueToIValue(Context context, Object value) {
		throw new RuntimeException("Unsupported convertJavaValueToIValue for " 
				+ this.getClass() + " and value type " + value.getClass().getSimpleName());
	}
	
	@Override
	public IValue getMemberValue(Context context, Identifier name) throws PromptoError {
		throw new SyntaxError("Cannot read member from " + this.getTypeName());
	}
	
	@Override
	public Set<IMethodDeclaration> getMemberMethods(Context context, Identifier name) throws PromptoError {
		return Collections.emptySet();
	}

	@Override
	public IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts) {
		throw new InvalidParameterException(value.toString());
	}
	
}
