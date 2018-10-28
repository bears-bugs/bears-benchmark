package prompto.declaration;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.CompilerException;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.FieldConstant;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.SyntaxError;
import prompto.expression.CategorySymbol;
import prompto.expression.IExpression;
import prompto.expression.Symbol;
import prompto.grammar.CategorySymbolList;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoEnum;
import prompto.intrinsic.PromptoSymbol;
import prompto.runtime.Context;
import prompto.transpiler.ITranspilable;
import prompto.transpiler.Transpiler;
import prompto.type.EnumeratedCategoryType;
import prompto.type.IType;
import prompto.type.ListType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;

public class EnumeratedCategoryDeclaration extends ConcreteCategoryDeclaration 
	implements IEnumeratedDeclaration<CategorySymbol> {
	
	CategorySymbolList symbolsList;
	Map<String, CategorySymbol> symbolsMap;
	EnumeratedCategoryType type;
	
	public EnumeratedCategoryDeclaration(Identifier name) {
		super(name);
	}
	
	public EnumeratedCategoryDeclaration(Identifier name, IdentifierList attrs, IdentifierList derived, CategorySymbolList symbols) {
		super(name, attrs, derived, null);
		this.type = new EnumeratedCategoryType(name);
		setSymbols(symbols);
	}
	
	@Override
	public DeclarationType getDeclarationType() {
		return DeclarationType.ENUMERATED;
	}
	
	@Override
	public CategorySymbolList getSymbolsList() {
		return symbolsList;
	}
	
	@Override
	public Map<String, CategorySymbol> getSymbolsMap() {
		return symbolsMap;
	}
	
	public void setSymbols(CategorySymbolList symbols) {
		this.symbolsMap = new HashMap<>();
		this.symbolsList = symbols;
		for(CategorySymbol s : symbols) {
			s.setType(type);
			symbolsMap.put(s.getName(), s);
		}
		symbols.setType(new ListType(type));
	}
	
	@Override
	public boolean hasAttribute(Context context, Identifier name) {
		if("name".equals(name.toString()))
			return true;
		else
			return super.hasAttribute(context, name);
	}
	
	@Override
	protected Set<Identifier> getLocalAttributes(Context context) {
		Set<Identifier> attributes = super.getLocalAttributes(context);
		if(attributes==null)
			attributes = new HashSet<>();
		Identifier nameId = new Identifier("name");
		if(!attributes.contains(nameId))
			attributes.add(nameId);
		return attributes;
	}
	
	
	
	@Override
	protected void toODialect(CodeWriter writer) {
		writer.append("enumerated category ");
		writer.append(getName());
		if(attributes!=null) {
			writer.append('(');
			attributes.toDialect(writer, true);
			writer.append(")");
		}
		if(derivedFrom!=null) {
			writer.append(" extends ");
			derivedFrom.toDialect(writer, true);
		}
		writer.append(" {\n");
		writer.indent();
		for(Symbol symbol : symbolsList) {
			((CategorySymbol)symbol).toDialect(writer);
			writer.append(";\n");
		}
		writer.dedent();
		writer.append("}\n");
	}

	@Override
	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(getName());
		writer.append(" as enumerated ");
		if(derivedFrom!=null)
			derivedFrom.toDialect(writer, true);
		else 
			writer.append("category");
		if(attributes!=null && attributes.size()>0) {
			if(attributes.size()==1)
				writer.append(" with attribute ");
			else
				writer.append(" with attributes ");
			attributes.toDialect(writer, true);
			writer.append(", and symbols:\n");
		} else
			writer.append(" with symbols:\n");
		writer.indent();
		for(Symbol symbol : symbolsList) {
			symbol.toDialect(writer);
			writer.append("\n");
		}
		writer.dedent();
	}
	
	@Override
	protected void toMDialect(CodeWriter writer) {
		writer.append("enum ");
		writer.append(getName());
		writer.append("(");
		if(derivedFrom!=null) {
			derivedFrom.toDialect(writer, false);
			if(attributes!=null && attributes.size()>0)
				writer.append(", ");
		}
		if(attributes!=null && attributes.size()>0)
			attributes.toDialect(writer, false);
		writer.append("):\n");
		writer.indent();
		for(Symbol symbol : symbolsList) {
			symbol.toDialect(writer);
			writer.append("\n");
		}
		writer.dedent();
	}

	@Override
	public void register(Context context) {
		context.registerDeclaration(this);
		for(Symbol s : symbolsList)
			s.register(context);
	}
	
	@Override
	public IType check(Context context, boolean isStart) {
		super.check(context, isStart);
		for(Symbol s : symbolsList)
			s.check(context); // TODO
		return getType(context);
	}
	
	@Override
	public EnumeratedCategoryType getType(Context context) {
		return new EnumeratedCategoryType(getId());
	}
	
	@Override
	protected ClassFile compileConcreteClass(Context context, String fullName) {
		try {
			java.lang.reflect.Type concreteType = CompilerUtils.categoryConcreteParentTypeFrom(fullName);
			ClassFile classFile = new ClassFile(concreteType);
			compileSuperClass(context, classFile, new Flags());
			compileInterface(context, classFile, new Flags());
			classFile.addInterface(PromptoEnum.class);
			compileCategoryField(context, classFile, new Flags());
			compileSymbolFields(context, classFile, new Flags());
			compileClassConstructor(context, classFile, new Flags());
			compileFields(context, classFile, new Flags());
			compileEmptyConstructor(context, classFile, new Flags());
			compileSuperConstructor(context, classFile, new Flags());
			compileMethods(context, classFile, new Flags());
			compileToString(context, classFile, new Flags());
			return classFile;
		} catch(SyntaxError e) {
			throw new CompilerException(e);
		}
	}

	private void compileToString(Context context, ClassFile classFile, Flags flags) {
		MethodInfo method = classFile.newMethod("toString", new Descriptor.Method(String.class));
		StackLocal local = method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		CompilerUtils.compileALOAD(method, local);
		MethodConstant mc = new MethodConstant(classFile.getThisClass(), "getText", String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, mc);
		method.addInstruction(Opcode.ARETURN);
	}

	private void compileSuperConstructor(Context context, ClassFile classFile, Flags flags) {
		if(isPromptoError(context))
			CompilerUtils.compileSuperConstructor(classFile, String.class);
	}

	@Override
	protected boolean needsClassConstructor() {
		return true;
	}
	

	@Override
	protected ClassConstant getSuperClass(Context context) {
		if("Error".equals(getName()))
			return new ClassConstant(RuntimeException.class);
		else
			return super.getSuperClass(context);
	}
	
	@Override
	public boolean isPromptoRoot(Context context) {
		return !isPromptoError(context);
	}
	
	@Override
	public boolean isPromptoError(Context context) {
		if("Error".equals(getName()))
			return true;
		else
			return super.isPromptoError(context);
	}


	@Override
	protected void compileClassConstructorBody(Context context, MethodInfo method, Flags flags) {
		if(isStorable())
			compilePopulateCategoryField(context, method, flags);
		for(CategorySymbol s : getSymbolsList())
			compilePopulateSymbolField(context, method, flags, s);
	}
	
	@Override
	protected void compileMethods(Context context, ClassFile classFile, Flags flags) {
		compileGetSymbolsMethod(context, classFile, new Flags());
		super.compileMethods(context, classFile, flags);
	}
	
	@Override
	protected void compileFields(Context context, ClassFile classFile, Flags flags) {
		super.compileFields(context, classFile, flags);
		Identifier name = new Identifier("name");
		if(!super.hasAttribute(context, name))
			compileField(context, classFile, flags, name);
	}
	
	private void compileGetSymbolsMethod(Context context, ClassFile classFile, Flags flags) {
		MethodInfo method = classFile.newMethod("getSymbols", new Descriptor.Method(List.class));
		method.addModifier(Modifier.STATIC);
		method.addInstruction(Opcode.LDC, classFile.getThisClass());
		MethodConstant m = new MethodConstant(PromptoSymbol.class, "getSymbols", 
				new Descriptor.Method(Class.class, List.class));
		method.addInstruction(Opcode.INVOKESTATIC, m);
		method.addInstruction(Opcode.ARETURN);
	}


	private void compilePopulateSymbolField(Context context, MethodInfo method, Flags flags, CategorySymbol symbol) {
		ClassConstant thisClass = method.getClassFile().getThisClass();
		java.lang.reflect.Type fieldType = getFieldType(context, thisClass.getType(), symbol); 
		if(fieldType==thisClass.getType())
			symbol.compileCallConstructor(context, method, flags);
		else
			symbol.compileInnerClassAndCallConstructor(context, method, flags, thisClass, fieldType);
		FieldConstant f = new FieldConstant(thisClass, symbol.getName(), fieldType);
		method.addInstruction(Opcode.PUTSTATIC, f);
	}

	private void compileSymbolFields(Context context, ClassFile classFile, Flags flags) {
		getSymbolsList().forEach((s)->
			compileSymbolField(context, classFile, flags, s));
	}

	private void compileSymbolField(Context context, ClassFile classFile, Flags flags, Symbol symbol) {
		java.lang.reflect.Type type = getFieldType(context, classFile.getThisClass().getType(), symbol);
		FieldInfo field = new FieldInfo(symbol.getName(), type);
		field.clearModifier(Modifier.PROTECTED);
		field.addModifier(Modifier.STATIC | Modifier.PUBLIC);
		classFile.addField(field);
	}

	private java.lang.reflect.Type getFieldType(Context context, java.lang.reflect.Type thisType, Symbol symbol) {
		if(isPromptoRoot(context))
			return thisType;
		else
			return CompilerUtils.getExceptionType(thisType, symbol.getName());
	}

	public ResultInfo compileGetMember(Context context, MethodInfo method, Flags flags, IExpression parent, Identifier id) {
		if("symbols".equals(id.toString())) {
			java.lang.reflect.Type concreteType = CompilerUtils.getCategoryEnumConcreteType(getId());
			String getterName = CompilerUtils.getterName("symbols");
			MethodConstant m = new MethodConstant(concreteType, getterName, List.class);
			method.addInstruction(Opcode.INVOKESTATIC, m);
			return new ResultInfo(List.class);
		} else
			throw new SyntaxError("No static member support for non-singleton " + this.getName());
	}

	@Override
	public void ensureDeclarationOrder(Context context, List<ITranspilable> list, Set<ITranspilable> set) {
	    if(set.contains(this))
	        return;
	    if (this.isUserError(context)) {
	        list.add(this);
	        set.add(this);
	        // don't declare inherited Error
	    } else
	        super.ensureDeclarationOrder(context, list, set);
	}

	private boolean isUserError(Context context) {
		return this.derivedFrom!=null && this.derivedFrom.size()==1 && this.derivedFrom.get(0).toString().equals("Error");
	}

	@Override
	public void declare(Transpiler transpiler) {
	    if("Error".equals(this.getName()))
	        return;
	    super.declare(transpiler);
	    transpiler.require("List");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    if (this.isUserError(transpiler.getContext()))
	        this.transpileUserError(transpiler);
	    else
	        this.transpileEnumerated(transpiler);
		return true;
	}

	private void transpileEnumerated(Transpiler transpiler) {
	    super.transpile(transpiler);
	    transpiler.newLine();
	    transpiler.append(this.getName()).append(".prototype.toString = function() { return this.name; };").newLine();
	    if(this.hasAttribute(transpiler.getContext(), new Identifier("text")))
	        transpiler.append(this.getName()).append(".prototype.getText = function() { return this.text; };").newLine();
	    else
	        transpiler.append(this.getName()).append(".prototype.getText = ").append(this.getName()).append(".prototype.toString;").newLine();
	    this.symbolsList.forEach(symbol -> symbol.initialize(transpiler));
	    this.transpileSymbols(transpiler);
	}

	private void transpileUserError(Transpiler transpiler) {
		if(transpiler.getEngine().supportsClass())
			transpileUserErrorClass(transpiler);
		else
			transpileUserErrorPrototype(transpiler);
	    this.symbolsList.forEach(symbol -> symbol.initializeError(transpiler));
	    this.transpileSymbols(transpiler);
}
	
	private void transpileUserErrorPrototype(Transpiler transpiler) {
	    transpiler.append("function ").append(this.getName()).append(" (values) {").indent()
	    	.append("if (!Error.captureStackTrace)").indent()
	    	.append("this.stack = (new Error()).stack;").dedent()
	    	.append("else").indent()
	    	.append("Error.captureStackTrace(this, this.constructor);").dedent()
	    	.append("this.message = values.text;").newLine()
	    	.append("this.promptoName = values.name;").newLine()
	    	.append("return this;").dedent()
	    	.append("}").newLine()
	    	.append(this.getName()).append(".prototype = Object.create(Error.prototype);").newLine()
	    	.append(this.getName()).append(".prototype.constructor = ").append(this.getName()).append(";").newLine()
	    	.append(this.getName()).append(".prototype.name = '").append(this.getName()).append("';").newLine()
	    	.append(this.getName()).append(".prototype.toString = function() { return this.message; };").newLine()
	    	.append(this.getName()).append(".prototype.getText = function() { return this.message; };").newLine();
	}

	private void transpileUserErrorClass(Transpiler transpiler) {
	    transpiler.append("class ").append(this.getName()).append(" extends Error {").indent();
	    transpiler.newLine();
	    transpiler.append("constructor(values) {").indent();
	    transpiler.append("super(values.text);").newLine();
	    transpiler.append("this.name = '").append(this.getName()).append("';").newLine();
	    transpiler.append("this.promptoName = values.name;").newLine();
	    if (this.attributes!=null) {
	        this.attributes.stream()
	            .filter(attr -> !("name".equals(attr.toString()) || "text".equals(attr.toString())))
	            .forEach(attr -> {
	                transpiler.append("this.").append(attr.toString()).append(" = values.hasownProperty('").append(attr.toString()).append("') ? values.").append(attr.toString()).append(" : null;");
	                transpiler.newLine();
	            });
	    }
	    transpiler.append("return this;").dedent();
	    transpiler.append("}").newLine();
	    transpiler.append("toString() {").indent().append("return this.message;").dedent().append("}").newLine();
	    transpiler.append("getText() {").indent().append("return this.message;").dedent().append("}").newLine();
	    transpiler.dedent().append("}").newLine();
	}

	private void transpileSymbols(Transpiler transpiler) {
	    Stream<String> names = this.symbolsList.stream().map(symbol ->symbol.getName());
	    transpiler.append(this.getName()).append(".symbols = new List(false, [").append(names.collect(Collectors.joining(", "))).append("]);").newLine();
	}


}
