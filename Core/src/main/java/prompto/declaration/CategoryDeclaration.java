package prompto.declaration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import prompto.compiler.ClassFile;
import prompto.compiler.Flags;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.grammar.Operator;
import prompto.runtime.Context;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.store.IStore;
import prompto.store.IStored;
import prompto.transpiler.ITranspilable;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;
import prompto.utils.TypeUtils;
import prompto.value.DbIdValue;
import prompto.value.IInstance;
import prompto.value.IValue;

public abstract class CategoryDeclaration extends BaseDeclaration {
	
	IdentifierList attributes;
	boolean storable = false;
	
	public CategoryDeclaration(Identifier id) {
		super(id);
	}

	public CategoryDeclaration(Identifier name, IdentifierList attributes) {
		super(name);
		this.attributes = attributes;
	}
	
	@Override
	public DeclarationType getDeclarationType() {
		return DeclarationType.CATEGORY;
	}
	
	public boolean isAWidget(Context context) {
		return false;
	}

	
	public void setStorable(boolean storable) {
		this.storable = storable;
	}
	
	@Override
	public boolean isStorable() {
		return storable;
	}

	public void setAttributes(IdentifierList attributes) {
		this.attributes = attributes;
	}
	
	public IdentifierList getAttributes() {
		return attributes;
	}
	
	public Set<Identifier> getAllAttributes(Context context) {
		if(attributes!=null)
			return new HashSet<Identifier>(attributes);
		else
			return null;
	}
		
	public abstract List<String> collectCategories(Context context);
	
	@Override
	public void register(Context context) {
		context.registerDeclaration(this);
		registerMethods(context);
	}
	
	protected abstract void registerMethods(Context context);
	

	@Override
	public IType check(Context context, boolean isStart) {
		if(attributes!=null) for(Identifier attribute : attributes) {
			if(attribute==null)
				continue; // problem already handled by parser
			AttributeDeclaration ad = context.getRegisteredDeclaration(AttributeDeclaration.class, attribute);
			if(ad==null)
				context.getProblemListener().reportUnknownAttribute(attribute.toString(), attribute);
		}
		return new CategoryType(this.getId());
	}
	
	@Override
	public CategoryType getType(Context context) {
		return new CategoryType(getId());
	}

	public boolean hasAttribute(Context context, Identifier name) {
		return attributes!=null && attributes.contains(name);
	}
	
	public boolean hasMethod(Context context, Identifier name) {
		return false; 
	}

	public boolean isDerivedFrom(Context context, CategoryType categoryType) {
		return false;
	}

	public IdentifierList getDerivedFrom() {
		return null;
	}

	public boolean isAbstract() {
		return false;
	}
	
	public abstract IInstance newInstance(Context context) throws PromptoError;
	
	public IInstance newInstance(Context context, IStored stored) throws PromptoError {
		IInstance instance = newInstance(context);
		instance.setMutable(true);
		try {
			populateInstance(context, stored, instance);
		} finally {
			instance.setMutable(false);
		}
		return instance;
	}

	private void populateInstance(Context context, IStored stored, IInstance instance) throws PromptoError {
		Object dbId = stored.getDbId();
		setDbId(context, instance, dbId);
		for(Identifier name : this.getAllAttributes(context)) 
			populateMember(context, stored, instance, name);
		if(instance.getStorable()!=null)
			instance.getStorable().setDirty(false);
	}
	
	protected void setDbId(Context context, IInstance instance, Object dbId) {
		IValue value = new DbIdValue(dbId);
		instance.setMember(context, new Identifier(IStore.dbIdName), value);
	}

	private void populateMember(Context context, IStored stored, IInstance instance, Identifier name) throws PromptoError {
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, name);
		if(!decl.isStorable())
			return;
		Object data = stored.getData(name.toString());
		populateMember(context, data, instance, decl);
	}
	
	protected void populateMember(Context context, Object data, IInstance instance, AttributeDeclaration decl) throws PromptoError {
		IValue value = data==null ? null : decl.getType().convertJavaValueToIValue(context, data);
		if(value!=null)
			instance.setMember(context, decl.getId(), value);
	}

	public void checkConstructorContext(Context context) {
		// nothing to do
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer = writer.newInstanceWriter(getType(writer.getContext()));
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}

	protected abstract void toEDialect(CodeWriter writer);

	protected void protoToEDialect(CodeWriter writer, boolean hasMethods, boolean hasMappings) {
		boolean hasAttributes = attributes!=null && attributes.size()>0;
		writer.append("define ");
		writer.append(getName());
		writer.append(" as ");
		if(storable)
			writer.append("storable ");
		categoryTypeToEDialect(writer);
		if(hasAttributes) {
			if(attributes.size()==1)
				writer.append(" with attribute ");
			else
				writer.append(" with attributes ");
			attributes.toDialect(writer, true);
		}
		if(hasMethods) {
			if(hasAttributes)
				writer.append(", and methods:");
			else 
				writer.append(" with methods:");
		} else if (hasMappings) {
			if(hasAttributes)
				writer.append(", and bindings:");
			else 
				writer.append(" with bindings:");
		}
		writer.newLine();	
	}
	
	protected void methodsToEDialect(CodeWriter writer, MethodDeclarationList methods) {
		writer.indent();
		for(IDeclaration decl : methods) {
			if(decl.getComments()!=null)
				decl.getComments().forEach(comment->comment.toDialect(writer));
			if(decl.getAnnotations()!=null)
				decl.getAnnotations().forEach(annotation->annotation.toDialect(writer));
			writer.newLine();
			CodeWriter w = writer.newMemberWriter();
			decl.toDialect(w);
		}
		writer.dedent();
	}

	protected void methodsToODialect(CodeWriter writer, MethodDeclarationList methods) {
		for(IDeclaration decl : methods) {
			if(decl.getComments()!=null)
				decl.getComments().forEach(comment->comment.toDialect(writer));
			if(decl.getAnnotations()!=null)
				decl.getAnnotations().forEach(annotation->annotation.toDialect(writer));
			CodeWriter w = writer.newMemberWriter();
			decl.toDialect(w);
			w.newLine();
		}
	}



	protected abstract void categoryTypeToEDialect(CodeWriter writer);

	protected abstract void toODialect(CodeWriter writer);
	
	protected void toODialect(CodeWriter writer, boolean hasBody) {
		categoryTypeToODialect(writer);
		writer.append(" ");
		writer.append(getName());
		if(attributes!=null) {
			writer.append('(');
			attributes.toDialect(writer, true);
			writer.append(')');
		}	
		categoryExtensionToODialect(writer);
		if(hasBody) {
			writer.append(" {\n");
			writer.newLine();
			writer.indent();
			bodyToODialect(writer);
			writer.dedent();
			writer.append('}');
			writer.newLine();
		} else
			writer.append(';');
	}

	protected abstract void categoryTypeToODialect(CodeWriter writer);

	protected void categoryExtensionToODialect(CodeWriter writer) {
		// by default no extension
	}

	protected abstract void bodyToODialect(CodeWriter writer);

	protected abstract void toMDialect(CodeWriter writer);

	protected void protoToMDialect(CodeWriter writer, IdentifierList derivedFrom) {
		if(storable)
			writer.append("storable ");
		categoryTypeToMDialect(writer);
		writer.append(" ");
		writer.append(getName());
		writer.append("(");
		if(derivedFrom!=null) {
			derivedFrom.toDialect(writer, false);
			if(attributes!=null)
				writer.append(", ");
		}
		if(attributes!=null)
			attributes.toDialect(writer, false);
		writer.append("):\n");
	}

	protected abstract void categoryTypeToMDialect(CodeWriter writer);

	public ClassFile compile(Context context, String fullName) {
		throw new UnsupportedOperationException(); // TODO -> abstract
	}
	
	public abstract IMethodDeclaration findOperator(Context context, Operator operator, IType type);
	
	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value) {
		return compileOperator(context, method, flags, left, value, Operator.PLUS);
	}
	
	public static ResultInfo compileDivide(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value) {
		return compileOperator(context, method, flags, left, value, Operator.DIVIDE);
	}

	public static ResultInfo compileIntDivide(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value) {
		return compileOperator(context, method, flags, left, value, Operator.IDIVIDE);
	}

	public static ResultInfo compileModulo(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value) {
		return compileOperator(context, method, flags, left, value, Operator.MODULO);
	}

	public static ResultInfo compileMultiply(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value) {
		return compileOperator(context, method, flags, left, value, Operator.MULTIPLY);
	}

	public static ResultInfo compileMultiply(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, ResultInfo right) {
		IType argType = TypeUtils.typeToIType(right.getType());
		return compileOperator(context, method, flags, left, right, argType, Operator.MULTIPLY);
	}

	public static ResultInfo compileMinus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value) {
		return compileOperator(context, method, flags, left, value, Operator.MINUS);
	}

	public static ResultInfo compileOperator(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression value, Operator oper) {
		IType argType = value.check(context);
		ResultInfo right = value.compile(context, method, flags);
		return compileOperator(context, method, flags, left, right, argType, oper);
	}

	private static ResultInfo compileOperator(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, ResultInfo right, IType argType, Operator oper) {
		String name = left.getType().getTypeName().substring("π.χ.".length());
		CategoryDeclaration decl = context.getRegisteredDeclaration(CategoryDeclaration.class, new Identifier(name));
		IMethodDeclaration operator = decl.findOperator(context, oper, argType);
		if(operator==null)
			throw new SyntaxError("No " + oper.getToken() + " operator method defined!");
		Context local = context.newInstanceContext(decl.getType(context), false).newChildContext();
		operator.registerArguments(local);
		IType resultType = operator.check(local, false);
		String methodName = "operator_" + oper.name();
		InterfaceConstant c = new InterfaceConstant(left.getType(), methodName, argType.getJavaType(context), resultType.getJavaType(context));
		method.addInstruction(Opcode.INVOKEINTERFACE, c);
		return new ResultInfo(resultType.getJavaType(context)); 
	}

	public MethodDeclarationMap getMemberMethods(Context context, Identifier name) {
		throw new UnsupportedOperationException(); // TODO -> abstract
	}

	public MethodDeclarationList getLocalMethods() {
		throw new UnsupportedOperationException(); // TODO -> abstract
	}

	public Map<String, MethodDeclarationMap> getAllMethods(Context context) {
		Map<String, MethodDeclarationMap> map = new HashMap<>();
		collectAllMethods(context, map);
		return map;
	}

	public void collectAllMethods(Context context, Map<String, MethodDeclarationMap> map) {
		getLocalMethods().forEach((m)->{
			MethodDeclarationMap current = map.get(m.getNameAsKey());
			if(current==null) {
				current = new MethodDeclarationMap(m.getId());
				map.put(m.getNameAsKey(), current);
			}
			if(current.get(m.getProto())==null)
				current.put(m.getProto(), m);
		});
	}

	protected boolean isPromptoRoot(Context context) {
		return false;
	}

	public abstract void ensureDeclarationOrder(Context context, List<ITranspilable> list, Set<ITranspilable> set);



	
}
