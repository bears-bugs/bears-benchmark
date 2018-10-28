package prompto.type;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.grammar.CmpOp;
import prompto.grammar.Identifier;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.utils.StringUtils;
import prompto.value.IValue;
import prompto.value.RangeBase;

import com.fasterxml.jackson.databind.JsonNode;

public interface IType extends ISection {
	
	Family getFamily();
	
	default String getTypeName() {
		return StringUtils.capitalizeFirst(getFamily().name());
	}
	
	default Identifier getTypeNameId() {
		return new Identifier(getTypeName());
	}
	Type getJavaType(Context context);

	IType checkAdd(Context context, IType other, boolean tryReverse);
	IType checkSubstract(Context context, IType other);
	IType checkDivide(Context context, IType other);
	IType checkIntDivide(Context context, IType other);
	IType checkMultiply(Context context, IType other, boolean tryReverse);
	IType checkModulo(Context context, IType rt);
	IType checkCompare(Context context, IType other, ISection section);
	IType checkItem(Context context, IType itemType) ;
	IType checkRange(Context context, IType other);
	IType checkContains(Context context, IType other);
	IType checkContainsAllOrAny(Context context, IType other);
	IType checkIterator(Context context);
	IType checkSlice(Context context);
	IType checkMember(Context context, Identifier name);
	
	void checkUnique(Context context);
	void checkExists(Context context);

	void checkAssignableFrom(Context context, IType other);
	boolean isAssignableFrom(Context context, IType other);
	boolean isMoreSpecificThan(Context context, IType other);
	
	RangeBase<?> newRange(Object first,Object last);
	Comparator<? extends IValue> getComparator(boolean descending);
	IValue getMemberValue(Context context, Identifier name) throws PromptoError;
	Set<IMethodDeclaration> getMemberMethods(Context context, Identifier name) throws PromptoError;


	String toString(Object value);
	void toDialect(CodeWriter writer);
	IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts);
	
	default IValue convertIValueToIValue(Context context, IValue value) {
		throw new UnsupportedOperationException("Cannot convertIValueToPromptoValue for " + this.getClass());
	}

	default IValue convertJavaValueToIValue(Context context, Object value) {
		throw new UnsupportedOperationException("Cannot convertJavaValueToPromptoValue for " + this.getClass());
	}
	
	default ResultInfo compileGetMember(Context context, MethodInfo method,
			Flags flags, IExpression parent, Identifier id) {
		throw new UnsupportedOperationException("Cannot compileGetMember for " + this.getClass());
	}

	default void compileConvertObjectToExact(Context context, MethodInfo method, Flags flags) {
		throw new UnsupportedOperationException("Cannot compileConvertObjectToExact for " + this.getClass());
	}

	default void compileGetStorableData(Context context, MethodInfo method, Flags flags) {
		// nothing to do
	}

	default void declare(Transpiler transpiler) { 
		throw new UnsupportedOperationException("declare " + this.getClass().getName()); 
	}
	
	default void transpile(Transpiler transpiler) {
		throw new UnsupportedOperationException("transpile " + this.getClass().getName()); 
	}


	default void declareAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) { 
		if(tryReverse)
			other.declareAdd(transpiler, this, false, right, left);
		else
			throw new UnsupportedOperationException("declareAdd " + this.getClass().getName());
	}

	default void transpileAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) { 
		if(tryReverse)
			other.transpileAdd(transpiler, this, false, right, left);
		else
			throw new UnsupportedOperationException("transpileAdd " + this.getClass().getName());
	}


	default String getTranspiledName(Context context) { 
		throw new UnsupportedOperationException("getTranspiledName " + this.getClass().getName());
	}

	default void transpileInstance(Transpiler transpiler) { 
		throw new UnsupportedOperationException("transpileInstance " + this.getClass().getName());
	}

	default void declareMember(Transpiler transpiler, String name) {
	    if(!"text".equals(name))
	        throw new UnsupportedOperationException("declareMember " + name + " for " + this.getClass().getName());

	}

	default void transpileMember(Transpiler transpiler, String name) {
	    if("text".equals(name))
          transpiler.append("getText()");
	    else
	        throw new UnsupportedOperationException("transpileMember " + name + " for " + this.getClass().getName());
	}

	default void declareModulo(Transpiler transpiler, IType other, IExpression left, IExpression right)  { 
		throw new UnsupportedOperationException("declareModulo " + this.getClass().getName());
	}

	default void transpileModulo(Transpiler transpiler, IType other, IExpression left, IExpression right) {
        throw new UnsupportedOperationException("transpileModulo " + this.getClass().getName());
	}

	default void declareDivide(Transpiler transpiler, IType other, IExpression left, IExpression right) { 
		throw new UnsupportedOperationException("declareDivide " + this.getClass().getName());
	}

	default void transpileDivide(Transpiler transpiler, IType other, IExpression left, IExpression right) {
        throw new UnsupportedOperationException("transpileDivide " + this.getClass().getName());
	}

	default void declareIntDivide(Transpiler transpiler, IType other, IExpression left, IExpression right) { 
		throw new UnsupportedOperationException("declareIntDivide " + this.getClass().getName());
	}

	default void transpileIntDivide(Transpiler transpiler, IType other, IExpression left, IExpression right) {
        throw new UnsupportedOperationException("transpileIntDivide " + this.getClass().getName());
	}

	default void declareMinus(Transpiler transpiler, IExpression expression) { 
		throw new UnsupportedOperationException("declareMinus " + this.getClass().getName());
	}

	default void transpileMinus(Transpiler transpiler, IExpression expression) {
        throw new UnsupportedOperationException("transpileMinus " + this.getClass().getName());
	}

	default void declareMultiply(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) { 
		if(tryReverse)
			other.declareMultiply(transpiler, this, false, right, left);
		else
			throw new UnsupportedOperationException("declareMultiply " + this.getClass().getName());
	}

	default void transpileMultiply(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) { 
		if(tryReverse)
			other.transpileMultiply(transpiler, this, false, right, left);
		else
			throw new UnsupportedOperationException("transpileMultiply " + this.getClass().getName());
	}

	default void declareSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right)  { 
		throw new UnsupportedOperationException("declareSubtract " + this.getClass().getName());
	}

	default void transpileSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right)  { 
		throw new UnsupportedOperationException("transpileSubtract " + this.getClass().getName());
	}

	default void transpileAssignMember(Transpiler transpiler, String name)   { 
		throw new UnsupportedOperationException("transpileAssignMember " + this.getClass().getName());
	}


	default void transpileAssignMemberValue(Transpiler transpiler, String name, IExpression expression) { 
		throw new UnsupportedOperationException("transpileAssignMemberValue " + this.getClass().getName());
	}

	default void transpileAssignItemValue(Transpiler transpiler, IExpression item, IExpression expression) { 
		throw new UnsupportedOperationException("transpileAssignItemValue " + this.getClass().getName());
	}


	default void declareSlice(Transpiler transpiler, IExpression first, IExpression last)   { 
		throw new UnsupportedOperationException("declareSlice " + this.getClass().getName());
	}

	default void transpileSlice(Transpiler transpiler, IExpression first, IExpression last) {
		throw new UnsupportedOperationException("transpileSlice " + this.getClass().getName());
	}

	default void declareCompare(Transpiler transpiler, IType other)   { 
		throw new UnsupportedOperationException("declareCompare " + this.getClass().getName());
	}

	default void transpileCompare(Transpiler transpiler, IType other, CmpOp operator, IExpression left, IExpression right) {
		throw new UnsupportedOperationException("transpileCompare " + this.getClass().getName());
	}

	default void declareItem(Transpiler transpiler, IType itemType, IExpression item) {
		throw new UnsupportedOperationException("declareItem " + this.getClass().getName());
	}

	default void transpileItem(Transpiler transpiler, IType itemType, IExpression item)  {
		throw new UnsupportedOperationException("transpileItem " + this.getClass().getName());
	}

	default void declareRange(Transpiler transpiler, IType lastType) {
		throw new UnsupportedOperationException("declareRange " + this.getClass().getName());
	}

	default void transpileRange(Transpiler transpiler, IExpression first, IExpression last) {
		throw new UnsupportedOperationException("transpileRange " + this.getClass().getName());
	}

	default void declareContains(Transpiler transpiler, IType other, IExpression container, IExpression item)  {
		throw new UnsupportedOperationException("declareContains " + this.getClass().getName());
	}

	default void transpileContains(Transpiler transpiler, IType other, IExpression container, IExpression item) {
		throw new UnsupportedOperationException("transpileContains " + this.getClass().getName());
	}

	default void declareContainsAllOrAny(Transpiler transpiler, IType other, IExpression container, IExpression items)  {
		throw new UnsupportedOperationException("declareContainsAllOrAny " + this.getClass().getName());
	}

	default void transpileContainsAll(Transpiler transpiler, IType other, IExpression container, IExpression items) {
		throw new UnsupportedOperationException("transpileContainsAll " + this.getClass().getName());
	}

	default void transpileContainsAny(Transpiler transpiler, IType other, IExpression container, IExpression items) {
		throw new UnsupportedOperationException("transpileContainsAny " + this.getClass().getName());
	}

	default void declareSorted(Transpiler transpiler, IExpression key) {
		throw new UnsupportedOperationException("declareSorted " + this.getClass().getName());
	}

	default void transpileSorted(Transpiler transpiler, boolean descending, IExpression key) {
		throw new UnsupportedOperationException("transpileSorted " + this.getClass().getName());
	}

	default void declareIterator(Transpiler transpiler, Identifier id, IExpression expression) {
		throw new UnsupportedOperationException("declareIterator " + this.getClass().getName());
	}

	default void transpileIterator(Transpiler transpiler, Identifier id, IExpression expression) {
		throw new UnsupportedOperationException("transpileIterator " + this.getClass().getName());
	}



}
 