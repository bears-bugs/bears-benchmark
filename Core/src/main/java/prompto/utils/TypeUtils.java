package prompto.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import prompto.declaration.CategoryDeclaration;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoDate;
import prompto.intrinsic.PromptoDateTime;
import prompto.intrinsic.PromptoDocument;
import prompto.intrinsic.PromptoPeriod;
import prompto.intrinsic.PromptoTime;
import prompto.intrinsic.PromptoVersion;
import prompto.runtime.Context;
import prompto.type.AnyType;
import prompto.type.BooleanType;
import prompto.type.CategoryType;
import prompto.type.CharacterType;
import prompto.type.DateTimeType;
import prompto.type.DateType;
import prompto.type.DecimalType;
import prompto.type.DocumentType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.MissingType;
import prompto.type.PeriodType;
import prompto.type.TextType;
import prompto.type.TimeType;
import prompto.type.UUIDType;
import prompto.type.VersionType;
import prompto.type.VoidType;
import prompto.value.IValue;

public abstract class TypeUtils {
	
	public static IType inferValuesType(Context context, Collection<? extends IValue> items) {
		Collection<IType> types = collectElementTypes(context, items);
		return inferCollectionType(context, types);
	}
	
	public static Collection<IType> collectElementTypes(Context context, Collection<? extends IValue> items) {
		List<IType> types = new ArrayList<IType>(items.size());
		for(IValue item : items)
			types.add(item.getType());
		return types;
	}
	
	public static IType inferCollectionType(Context context, Collection<IType> types) {
		if(types.isEmpty())
			return MissingType.instance();
		IType lastType = null;
		for(IType type : types) {
			if(lastType==null)
				lastType = type;
			else if(!lastType.equals(type)) { 
				if(lastType.isAssignableFrom(context, type))
					; // lastType is less specific
				else if(type.isAssignableFrom(context, lastType))
					lastType = type; // elemType is less specific
				else {
					IType common = inferCommonRootType(context, lastType, type);
					if(common!=null)
						lastType = common;
					else
						throw new SyntaxError("Incompatible types: " + type.toString() + " and " + lastType.toString());
				}
					
			}
		}
		return lastType; 
	}
	
	

	public static IType inferCommonRootType(Context context, IType type1, IType type2) {
		if(type1 instanceof CategoryType && type2 instanceof CategoryType)
			return inferCommonRootType(context, (CategoryType)type1, (CategoryType)type2, true);
		else
			return null;
	}
	
	
	public static IType inferCommonRootType(Context context, CategoryType type1, CategoryType type2, boolean trySwap) {
		CategoryDeclaration decl1 = context.getRegisteredDeclaration(CategoryDeclaration.class, type1.getTypeNameId());
		if(decl1.getDerivedFrom()!=null) {
			for(Identifier id : decl1.getDerivedFrom()) {
				CategoryType parentType = new CategoryType(id);
				if(parentType.isAssignableFrom(context, type2))
					return parentType;
			}
			// climb up the tree
			for(Identifier id : decl1.getDerivedFrom()) {
				CategoryType parentType = new CategoryType(id);
				IType commonType = inferCommonRootType(context, parentType, type2);
				if(commonType!=null)
					return commonType;
			}
		}
		if(trySwap)
			return inferCommonRootType(context, type2, type1, false);
		else
			return null;
	}

	public static IType inferElementType(Context context, ExpressionList expressions) {
		Collection<IType> types = collectElementTypes(context, expressions);
		return inferCollectionType(context, types);
	}

	private static Collection<IType> collectElementTypes(Context context, ExpressionList expressions) {
		List<IType> types = new ArrayList<IType>(expressions.size());
		for(IExpression expression : expressions)
			types.add(expression.check(context));
		return types;
	}

	static final Map<Type,IType> typeToITypeMap = createTypeToITypeMap();
	
	static Map<Type, IType> createTypeToITypeMap() {
		Map<Type,IType> map = new HashMap<Type, IType>();
		map.put(void.class, VoidType.instance());
		map.put(boolean.class, BooleanType.instance());
		map.put(Boolean.class, BooleanType.instance());
		map.put(char.class, CharacterType.instance());
		map.put(Character.class, CharacterType.instance());
		map.put(int.class, IntegerType.instance());
		map.put(Integer.class, IntegerType.instance());
		map.put(long.class, IntegerType.instance());
		map.put(Long.class, IntegerType.instance());
		map.put(double.class, DecimalType.instance());
		map.put(Double.class, DecimalType.instance());
		map.put(String.class, TextType.instance());
		map.put(UUID.class, UUIDType.instance());
		map.put(PromptoDate.class, DateType.instance());
		map.put(PromptoTime.class, TimeType.instance());
		map.put(PromptoDateTime.class, DateTimeType.instance());
		map.put(PromptoPeriod.class, PeriodType.instance());
		map.put(PromptoVersion.class, VersionType.instance());
		map.put(PromptoDocument.class, DocumentType.instance());
		map.put(Object.class, AnyType.instance());
		return map;
	}

	public static IType typeToIType(Type type) {
		return typeToITypeMap.get(type);
	}

}
