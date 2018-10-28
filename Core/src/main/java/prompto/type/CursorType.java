package prompto.type;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import prompto.compiler.Descriptor;
import prompto.compiler.Flags;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.declaration.BuiltInMethodDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.grammar.Identifier;
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoList;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.Cursor;
import prompto.value.IValue;

public class CursorType extends IterableType {
	
	public CursorType(IType itemType) {
		super(Family.CURSOR, itemType, "Cursor<" + itemType.getTypeName()+">");
	}
	
	@Override
	public IterableType withItemType(IType itemType) {
		return new CursorType(itemType);
	}
	
	@Override
	public Type getJavaType(Context context) {
		return IterableWithCounts.class;
	}
	
	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return super.isAssignableFrom(context, other) ||
				(other instanceof CursorType) && 
				itemType.isAssignableFrom(context, ((CursorType)other).getItemType());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true; 
		if(!(obj instanceof CursorType))
			return false;
		CursorType other = (CursorType)obj;
		return this.getItemType().equals(other.getItemType());
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
        else if ("totalCount".equals(name))
            return IntegerType.instance();
        else
    		return super.checkMember(context, id);
   }
	
	@Override
	public void declareMember(Transpiler transpiler, String name) {
	    if(!"count".equals(name) && !"totalCount".equals(name))
	        super.declareMember(transpiler, name);
	}

	@Override
	public void transpileMember(Transpiler transpiler, String name) {
	    if("count".equals(name) || "totalCount".equals(name))
	    	transpiler.append(name);
	    else
	    	super.transpileMember(transpiler, name);
	}
	
	@Override
	public Set<IMethodDeclaration> getMemberMethods(Context context, Identifier id) throws PromptoError {
		switch(id.toString()) {
		case "toList":
			return new HashSet<>(Collections.singletonList(TO_LIST_METHOD));
		default:
			return super.getMemberMethods(context, id);
		}
	}

	final IMethodDeclaration TO_LIST_METHOD = new BuiltInMethodDeclaration("toList") {
		
		@Override
		public IValue interpret(Context context) throws PromptoError {
			Cursor value = (Cursor)getValue(context);
			return value.toListValue();
		};
		
		
		
		@Override
		public IType check(Context context, boolean isStart) {
			return new ListType(itemType);
		}

		public boolean hasCompileExactInstanceMember() {
			return true;
		}
		
		public prompto.compiler.ResultInfo compileExactInstanceMember(Context context, MethodInfo method, Flags flags, prompto.grammar.ArgumentAssignmentList assignments) {
			// call replace method
			Descriptor.Method descriptor = new Descriptor.Method(PromptoList.class);
			InterfaceConstant constant = new InterfaceConstant(IterableWithCounts.class, "toList", descriptor);
			method.addInstruction(Opcode.INVOKEINTERFACE, constant);
			// done
			return new ResultInfo(PromptoList.class);

		}
		
		public void transpileCall(Transpiler transpiler, prompto.grammar.ArgumentAssignmentList assignments) {
			transpiler.append("toList()");
		}
	};

}
