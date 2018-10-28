package prompto.instance;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.NotMutableError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.expression.ItemSelector;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoAny;
import prompto.intrinsic.PromptoDict;
import prompto.intrinsic.PromptoDocument;
import prompto.intrinsic.PromptoList;
import prompto.intrinsic.PromptoTuple;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.AnyType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IContainer;
import prompto.value.IValue;

public class ItemInstance implements IAssignableSelector {

	IAssignableInstance parent;
	IExpression item;
	
	public ItemInstance(IExpression item) {
		this.item = item;
	}
	
	@Override
	public void setParent(IAssignableInstance parent) {
		this.parent = parent;
	}
	
	public IExpression getItem() {
		return item;
	}
	
	@Override
	public void toDialect(CodeWriter writer, IExpression expression) {
		parent.toDialect(writer, null);
		writer.append('[');
		item.toDialect(writer);
		writer.append(']');
	}
	
	@Override
	public IType checkAssignValue(Context context, IType valueType) {
		// called when a[3] = value
		IType itemType = item.check(context);
		return parent.checkAssignItem(context, itemType, valueType);
	}
	
	@Override
	public IType checkAssignMember(Context context, Identifier memberName, IType valueType) {
		// called when a[3].member = value
		return AnyType.instance(); // TODO 
	}
	
	@Override
	public IType checkAssignItem(Context context, IType itemType, IType valueType) {
		// called when a[3][x] = value
		IType thisItemType = item.check(context);
		IType parentType = parent.checkAssignItem(context, thisItemType, valueType);
		return parentType.checkItem(context, itemType); 
	}
	
	@Override
	public void assign(Context context, IExpression expression) throws PromptoError {
		IValue root = parent.interpret(context);
		if(!root.isMutable())
			throw new NotMutableError();
		IValue idx = item.interpret(context);
		IValue value = expression.interpret(context);
		root.setItem(context, idx, value);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue parent = this.parent.interpret(context);
		IValue item = this.item.interpret(context);
        if (parent instanceof IContainer)
            return ((IContainer<?>)parent).getItem(context, item);
         else
 			throw new SyntaxError("Unknown item/key: " + item.getClass().getName());
	}
	
	@Override
	public ResultInfo compileParent(Context context, MethodInfo method, Flags flags) {
		ResultInfo parentInfo = this.parent.compileParent(context, method, flags);
		return ItemSelector.compileGetItem(context, method, flags, parentInfo, item);
	}
	
	@Override
	public ResultInfo compileAssign(Context context, MethodInfo method, Flags flags, IExpression value) {
		ResultInfo parentInfo = this.parent.compileParent(context, method, flags);
		if(PromptoAny.class==parentInfo.getType() || PromptoDocument.class==parentInfo.getType())
			return compileAssignAny(context, method, flags, item, value);
		else if(PromptoList.class==parentInfo.getType())
			return compileAssignList(context, method, flags, item, value);
		else if(PromptoTuple.class==parentInfo.getType())
			return compileAssignTuple(context, method, flags, item, value);
		else if(PromptoDict.class==parentInfo.getType())
			return compileAssignDict(context, method, flags, item, value);
		else 
			throw new UnsupportedOperationException("Cannot compileAssign for " + parentInfo.getType().getTypeName());
	}
	
	private ResultInfo compileAssignList(Context context, MethodInfo method, Flags flags, IExpression item2, IExpression value) {
		ResultInfo itemInfo = item.compile(context, method, flags.withPrimitive(true));
		CompilerUtils.numberToint(method, itemInfo);
		method.addInstruction(Opcode.ICONST_M1);
		method.addInstruction(Opcode.IADD);
		value.compile(context, method, flags.withPrimitive(false));
		IOperand oper = new MethodConstant(PromptoList.class, "set", int.class, Object.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume result
		return new ResultInfo(void.class);
	}

	private ResultInfo compileAssignTuple(Context context, MethodInfo method, Flags flags, IExpression item2, IExpression value) {
		ResultInfo itemInfo = item.compile(context, method, flags.withPrimitive(true));
		CompilerUtils.numberToint(method, itemInfo);
		method.addInstruction(Opcode.ICONST_M1);
		method.addInstruction(Opcode.IADD);
		value.compile(context, method, flags.withPrimitive(false));
		IOperand oper = new MethodConstant(PromptoTuple.class, "set", int.class, Object.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume result
		return new ResultInfo(void.class);
	}

	private ResultInfo compileAssignDict(Context context, MethodInfo method, Flags flags, IExpression item2, IExpression value) {
		item.compile(context, method, flags.withPrimitive(false));
		value.compile(context, method, flags.withPrimitive(false));
		IOperand oper = new MethodConstant(PromptoDict.class, "put", Object.class, Object.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume result
		return new ResultInfo(void.class);
	}

	private ResultInfo compileAssignAny(Context context, MethodInfo method, Flags flags, IExpression item, IExpression value) {
		item.compile(context, method, flags.withPrimitive(false));
		value.compile(context, method, flags.withPrimitive(false));
		IOperand oper = new MethodConstant(PromptoAny.class, "setItem", Object.class, Object.class, Object.class, void.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(void.class);
	}
	
	@Override
	public IType check(Context context) {
		IType parentType = this.parent.check(context);
		IType itemType = this.item.check(context);
	    return parentType.checkItem(context, itemType);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.parent.declare(transpiler);
	    this.item.declare(transpiler);
	}
	
	@Override
	public void declareAssign(Transpiler transpiler, IExpression expression) {
	    this.parent.declare(transpiler);
	    this.item.declare(transpiler);
	    expression.declare(transpiler);
	}
	
	
	@Override
	public void transpileAssign(Transpiler transpiler, IExpression expression) {
	    IType parentType = this.parent.check(transpiler.getContext());
	    this.parent.transpileAssignParent(transpiler);
	    parentType.transpileAssignItemValue(transpiler, this.item, expression);
	}
	
	@Override
	public void transpileAssignParent(Transpiler transpiler) {
	    this.parent.transpileAssignParent(transpiler);
	    transpiler.append(".getItem(");
	    this.item.transpile(transpiler);
	    transpiler.append(", true)");
	}
}
