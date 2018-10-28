package prompto.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import prompto.declaration.AnyNativeCategoryDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.intrinsic.PromptoDict;
import prompto.intrinsic.PromptoDocument;
import prompto.intrinsic.PromptoList;
import prompto.intrinsic.PromptoSet;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.type.AnyType;
import prompto.type.BaseType;
import prompto.type.DictType;
import prompto.type.DocumentType;
import prompto.type.IType;
import prompto.type.IteratorType;
import prompto.type.ListType;
import prompto.type.SetType;
import prompto.type.TextType;
import prompto.utils.TypeUtils;
import prompto.value.Dictionary;
import prompto.value.Document;
import prompto.value.IValue;
import prompto.value.IteratorValue;
import prompto.value.ListValue;
import prompto.value.NativeInstance;
import prompto.value.SetValue;
import prompto.value.Text;


public class JavaClassType extends BaseType {
	
	Type type;
	
	public JavaClassType(Type type) {
		super(Family.CLASS);
		this.type = type;
	}
	
	@Override
	public Type getJavaType(Context context) {
		return toJavaClass(type);
	}
	
	private static Class<?> toJavaClass(Type type) {
		if(type instanceof Class<?>)
			return (Class<?>)type;
		else if (type instanceof ParameterizedType)
			return toJavaClass(((ParameterizedType)type).getRawType());
		else
			throw new RuntimeException("Unsupported:" + type.getClass());
	}

	public IType convertJavaClassToPromptoType(Context context, IType returnType) {
		return convertJavaClassToPromptoType(context, type, returnType);
	}
	
	private static IType convertJavaClassToPromptoType(Context context, Type type, IType returnType) {
		IType result = TypeUtils.typeToIType(type);
		if(result!=null)
			return result;
		result = convertParameterizedType(context, type); 
		if(result!=null)
			return result;
		NativeCategoryDeclaration decl = context.getNativeBinding(type);
		if(decl!=null)
			return decl.getType(context);
		else if(returnType==AnyType.instance())
			return returnType;
		else
			return null;
	}
	
    private static IType convertParameterizedType(Context context, Type type) {
		if(type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType)type;
			Type rawType = ptype.getRawType();
			if(rawType instanceof Class<?>) {
				if(PromptoDocument.class.isAssignableFrom((Class<?>)rawType))
					return DocumentType.instance();
				else if(List.class.isAssignableFrom((Class<?>)rawType)) {
					Type elemType = ptype.getActualTypeArguments()[0];
					IType itemType = convertJavaClassToPromptoType(context, elemType, null);
					return new ListType(itemType);
				} else if(Set.class.isAssignableFrom((Class<?>)rawType)) {
					Type elemType = ptype.getActualTypeArguments()[0];
					IType itemType = convertJavaClassToPromptoType(context, elemType, null);
					return new SetType(itemType);
				} else if(Iterator.class.isAssignableFrom((Class<?>)rawType)) {
					Type elemType = ptype.getActualTypeArguments()[0];
					IType itemType = convertJavaClassToPromptoType(context, elemType, null);
					return new IteratorType(itemType);
				} else if(Map.class.isAssignableFrom((Class<?>)rawType)) {
					Type elemType = ptype.getActualTypeArguments()[1];
					IType itemType = convertJavaClassToPromptoType(context, elemType, null);
					return new DictType(itemType);
				} 
			}
		}
		return null;
	}

	private static Type nthArgTypeFromParameterizedType(Type type, int index) {
		if(type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType)type;
			return ptype.getActualTypeArguments()[index];	
		} else
			return Object.class;
	}

	public IValue convertJavaValueToPromptoValue(Context context, Object value, IType returnType) {
		return convertJavaValueToPromptoValue(context, value, type, returnType);
    }
    
    public static IValue convertJavaValueToPromptoValue(Context context, Object value, Type type, IType returnType) {
    	IValue val = convertIValue(value);
    	if(val!=null)
    		return val;
    	val = convertNative(context, value, type);
    	if(val!=null)
    		return val;
    	val = convertCategory(context, value, type, returnType);
    	if(val!=null)
    		return val;
    	val = convertDocument(context, value, type, returnType);
    	if(val!=null)
    		return val;
    	val = convertList(context, value, type, returnType);
    	if(val!=null)
    		return val;
    	val = convertSet(context, value, type, returnType);
    	if(val!=null)
    		return val;
    	val = convertDict(context, value, type, returnType);
    	if(val!=null)
    		return val;
    	val = convertIterator(context, value, type, returnType);
    	if(val!=null)
    		return val;
    	if(returnType==AnyType.instance())
	    	return new NativeInstance(AnyNativeCategoryDeclaration.getInstance(), value);
	    else
	        throw new InternalError("Unable to convert:" + value.getClass().getSimpleName());
    }

	private static IValue convertDocument(Context context, Object value, Type type, IType returnType) {
		if(value instanceof PromptoDocument<?,?>) {
			if(returnType==DocumentType.instance() || returnType==AnyType.instance()) {
				return new Document(context, (PromptoDocument<?,?>)value);
			}
		}
		return null;
	}

	private static IValue convertIValue(Object value) {
	    return value instanceof IValue ? (IValue)value : null;
	}

	private static IValue convertNative(Context context, Object value, Type type) {
        IType itype = TypeUtils.typeToIType(type);
        return itype != null ? itype.convertJavaValueToIValue(context, value) : null;
	}


	private static IValue convertCategory(Context context, Object value, Type type, IType returnType) {
		// ensure the underlying declaration is loaded
		context.getRegisteredDeclaration(IDeclaration.class, returnType.getTypeNameId());
 		NativeCategoryDeclaration decl = context.getNativeBinding(type);
		return decl!=null ? new NativeInstance(decl, value) : null;
	}

	@SuppressWarnings("unchecked")
	private static IValue convertList(Context context, Object value, Type type, IType returnType) {
		if(value instanceof List<?>) {
			if(returnType instanceof ListType || returnType==AnyType.instance()) {
				Type elemType = nthArgTypeFromParameterizedType(type, 0);
				IType itemType = returnType instanceof ListType ? ((ListType)returnType).getItemType() : AnyType.instance();
				PromptoList<IValue> list = new PromptoList<IValue>(false);
				for(Object obj : (List<Object>)value) {
					IValue val = convertJavaValueToPromptoValue(context, obj, elemType, itemType);
					list.add(val);
				}
				return new ListValue(itemType, list);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static IValue convertSet(Context context, Object value, Type type, IType returnType) {
		if(!(value instanceof Set<?>) || !(returnType instanceof SetType))
			return null;
		Type elemType = nthArgTypeFromParameterizedType(type, 0);
		IType itemType = ((SetType)returnType).getItemType();
		PromptoSet<IValue> set = new PromptoSet<IValue>();
		for(Object obj : (Set<Object>)value) {
			IValue val = convertJavaValueToPromptoValue(context, obj, elemType, itemType);
			set.add(val);
		}
		return new SetValue(itemType, set);
	}

	@SuppressWarnings("unchecked")
	private static IValue convertDict(Context context, Object value, Type type, IType returnType) {
		if(!(value instanceof Map) || !(returnType instanceof DictType))
			return null;
		Type keyType = nthArgTypeFromParameterizedType(type, 0);
		Type elemType = nthArgTypeFromParameterizedType(type, 1);
		IType itemType = ((DictType)returnType).getItemType();
		PromptoDict<Text, IValue> dict = new PromptoDict<Text, IValue>(true);
		for(Object obj : ((Map<Object,Object>)value).keySet()) {
			Object val = ((Map<Object,Object>)value).get(obj);
			Text key = (Text)convertJavaValueToPromptoValue(context, obj, keyType, TextType.instance());
			IValue ivalue = convertJavaValueToPromptoValue(context, val, elemType, itemType);
			dict.put(key, ivalue);
		}
		dict.setMutable(false); // TODO: mutable in return type ?
		return new Dictionary(itemType, dict); 
	}
	
	private static IValue convertIterator(Context context, Object value, Type type, IType returnType) {
		if(!(value instanceof Iterator) || !(returnType instanceof IteratorType))
			return null;
		Iterator<?> source = (Iterator<?>)value;
		Type elemType = nthArgTypeFromParameterizedType(type, 0);
		IType itemType = ((IteratorType)returnType).getItemType();
		Iterator<IValue> convertingIterator = new Iterator<IValue>() {
			@Override public boolean hasNext() { return source.hasNext(); }
			@Override public IValue next() {
				Object val = source.next();
				return convertJavaValueToPromptoValue(context, val, elemType, itemType);
			}
		};
		return new IteratorValue(itemType, convertingIterator);
	}


	@Override
	public void checkUnique(Context context) { }

	@Override
	public void checkExists(Context context) { }

	@Override
	public boolean isAssignableFrom(Context context, IType other) { return false; }

	@Override
	public boolean isMoreSpecificThan(Context context, IType other) { return true; }



}