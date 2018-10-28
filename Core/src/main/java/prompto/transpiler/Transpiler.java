package prompto.transpiler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import prompto.declaration.CategoryDeclaration;
import prompto.declaration.IDeclaration;
import prompto.runtime.Context;
import prompto.type.CategoryType;
import prompto.utils.Logger;
import prompto.utils.ResourceUtils;

public class Transpiler {

	static final Logger logger = new Logger();

	IJSEngine engine;
	Context context;
	Transpiler parent;
	Set<ITranspilable> declared;
	Set<String> required;
	Stack<String> lines;
	StringBuilder line;
	String indents;
	String getterName;
	String setterName;
	
	public Transpiler(IJSEngine engine, Context context) {
		this.engine = engine;
		this.context = context;
		this.declared = new HashSet<>();
	    this.required = new HashSet<>();
	    this.lines = new Stack<>();
	    this.line = new StringBuilder();
	    this.indents = "";
	    this.getterName = null;
	    this.setterName = null;
	    // load polyfills
		this.require("Utils");
		engine.getPolyfills().forEach(poly->this.require(poly));
 	}
	
	public IJSEngine getEngine() {
		return engine;
	}
	
	public Context getContext() {
		return context;
	}
	
	public String getGetterName() {
		return getterName;
	}

	public String getSetterName() {
		return setterName;
	}
	
	public Transpiler newLocalTranspiler() {
		Context context = this.context.newLocalContext();
		return this.copyTranspiler(context);
	}

	public Transpiler newChildTranspiler(Context calling) {
		if(calling==null)
			calling = this.context.newChildContext();
		return this.copyTranspiler(calling);
	}
	
	public Transpiler newInstanceTranspiler(CategoryType categoryType) {
		Context context = this.context.newInstanceContext(categoryType, true);
		return this.copyTranspiler(context);
	}
	
	
	public Transpiler newResourceTranspiler() {
		Context context = this.context.newResourceContext();
	    return this.copyTranspiler(context);
	}



	public Transpiler newMemberTranspiler(CategoryType categoryType) {
		Context context = this.context.newMemberContext(categoryType);
	    return this.copyTranspiler(context);
	}

	public Transpiler newGetterTranspiler(CategoryType categoryType, String name) {
		Transpiler transpiler = this.newMemberTranspiler(categoryType);
	    transpiler.getterName = name;
	    return transpiler;
	}

	public Transpiler newSetterTranspiler(CategoryType categoryType, String name) {
		Transpiler transpiler = this.newMemberTranspiler(categoryType);
	    transpiler.setterName = name;
	    return transpiler;
	}



	public Transpiler copyTranspiler(Context context) {
		Transpiler transpiler = new Transpiler(this.engine, context);
	    transpiler.declared = this.declared;
	    transpiler.required = this.required;
	    transpiler.lines = this.lines;
	    transpiler.line = this.line;
	    transpiler.indents = this.indents;
	    transpiler.parent = this;
	    return transpiler;
	}
	
	public Transpiler append(String value) {
		this.line.append(value);
		return this;
	}
	
	public Transpiler append(char value) {
		this.line.append(value);
		return this;
	}

	public Transpiler append(boolean value) {
		this.line.append(value);
		return this;
	}
	
	public Transpiler indent() {
		return indent(false);
	}

	public Transpiler indent(boolean indentOnly) {
	   if(!indentOnly)
	        this.lines.push(this.line.toString());
	    this.indents += '\t';
	    this.line = new StringBuilder(this.indents);
	    return this;
	}
	
	public Transpiler dedent() {
	    String line = this.line.toString();
	    if(!(line.equals(this.indents)))
	        this.lines.push(line);
	    if(this.indents.isEmpty()) {
	        throw new RuntimeException("Illegal dedent!");
	    }
	    this.indents = this.indents.substring(1);
	    this.line = new StringBuilder(this.indents);
	    return this;
	}
	
	public Transpiler newLine() {
		this.lines.push(this.line.toString());
		this.line = new StringBuilder(this.indents);
		return this;
	}

	public Transpiler trimLast(int count) {
	    this.line.setLength(this.line.length() - count);
	    return this;
	}

	
	public Transpiler printTestName(String testName) {
		this.append("print(\"\\\"").append(testName.substring(1, testName.length() - 1)).append("\\\" test ");
		return this;
	}


	public void flush() {
	    if(this.parent!=null) {
	        this.parent.line = this.line;
	        this.parent.indents = this.indents;
	    }
	}

	public void declare(ITranspilable transpilable) {
		if(transpilable instanceof IDeclaration && ((IDeclaration)transpilable).hasAnnotation(context, "Inlined"))
			return;
		declared.add(transpilable);
	}
	
	
	public Set<ITranspilable> getDeclared() {
		return declared;
	}

	public void require(String script) {
		required.add(script);
	}
	
	public Set<String> getRequired() {
		return required;
	}
	
	public boolean requires(String script) {
		return required.contains(script);
	}

	
	@Override
	public String toString() {
	    this.appendAllRequired();
	    this.appendAllDeclared();
	    this.flush();
	    return this.lines.stream().collect(Collectors.joining("\n"));
	}
	
	public void print(PrintWriter printer) {
	    this.appendAllRequired();
	    this.appendAllDeclared();
	    this.flush();
	    this.lines.forEach(printer::println);
	}


	private void appendAllDeclared() {
		List<ITranspilable> list = new ArrayList<>();
		Set<ITranspilable> set = new HashSet<>();
	    this.declared.forEach(decl -> {
	        if(decl instanceof CategoryDeclaration)
	            ((CategoryDeclaration)decl).ensureDeclarationOrder(this.context, list, set);
	        else
	            list.add(decl);
	    });
	    list.forEach(this::appendOneDeclared);
	}

	private void appendOneDeclared(ITranspilable decl) {
		Transpiler transpiler = this.newLocalTranspiler();
	    decl.transpile(transpiler);
	    transpiler.flush();
	    String line = this.line.toString();
	    if(!(line.equals(this.indents))) {
	        this.lines.push(line);
	        this.line = new StringBuilder(this.indents);
	    }
	    this.lines.push("");
	}
	
	private void appendAllRequired() {
		required.forEach(this::appendOneRequired);
	}

	private void appendOneRequired(String module) {
		String result = getResourceAsString("prompto/intrinsic/" + module + ".js");
		if(result==null)
			result = getResourceAsString("polyfills/" + module + ".js");
		if(result!=null)
			this.lines.push(result);
		else
			throw new RuntimeException("Could not locate script: " + module);
	}

	private String getResourceAsString(String path) {
		try {
			return ResourceUtils.getResourceAsString(path);
		} catch(Throwable t) {
			return null;
		}
	}

	

}
