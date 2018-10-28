package prompto.type;

import java.lang.reflect.Type;
import java.util.Iterator;

import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.store.Family;

public class IteratorType extends IterableType {

	public IteratorType(IType itemType) {
		super(Family.ITERATOR, itemType, "Iterator<" + itemType.getTypeName()+">");
	}
	
	@Override
	public IterableType withItemType(IType itemType) {
		return new IteratorType(itemType);
	}

	@Override
	public Type getJavaType(Context context) {
		return Iterator.class;
	}
	
	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return super.isAssignableFrom(context, other) ||
				(other instanceof IteratorType && 
				itemType.isAssignableFrom(context, ((IteratorType)other).getItemType()));
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true; 
		return (obj instanceof IteratorType
				&& this.getItemType().equals(((IteratorType)obj).getItemType()));
	}
	
	@Override
	public IType checkIterator(Context context) {
		return itemType;
	}

	@Override
	public IType checkMember(Context context, Identifier id) {
		String name = id.toString();
        if ("count".equals(name))
            return IntegerType.instance();
        else
    		return super.checkMember(context, id);
   }

	
}
