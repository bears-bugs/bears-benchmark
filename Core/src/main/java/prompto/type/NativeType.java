package prompto.type;

import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;

public abstract class NativeType extends BaseType {

	private static NativeType[] all = null;
	
	public static NativeType[] getAll() {
		if(all==null) {
			all = new NativeType[] {
					AnyType.instance(),
					BooleanType.instance(),
					IntegerType.instance(),
					DecimalType.instance(),
					CharacterType.instance(),
					TextType.instance(),
					CodeType.instance(),
					DateType.instance(),
					TimeType.instance(),
					DateTimeType.instance(),
					PeriodType.instance(),
					DocumentType.instance(),
					TupleType.instance(),
					UUIDType.instance()
				};
		}
		return all;
	}
	
	public NativeType(Family family) {
		super(family);
	}
	
	
	@Override
	public IType checkMember(Context context, Identifier name) {
		if("text".equals(name.toString()))
			return TextType.instance();
		else
			return super.checkMember(context, name);
	}
	
	@Override
	public void checkUnique(Context context) {
		// nothing to do
	}
	
	@Override
	public void checkExists(Context context) {
		// nothing to do
	}
	
	@Override
	public boolean isMoreSpecificThan(Context context, IType other) {
		return false;
	}
	
	@Override
	public void declareSorted(Transpiler transpiler, IExpression key) {
		// nothing to do
	}

	@Override
	public void transpileSorted(Transpiler transpiler, boolean descending, IExpression key) {
	    if(descending)
	        transpiler.append("function(o1, o2) { return o1 === o2 ? 0 : o1 > o2 ? -1 : 1; }");
	}


}
