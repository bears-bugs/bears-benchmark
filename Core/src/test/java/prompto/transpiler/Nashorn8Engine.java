package prompto.transpiler;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.grammar.Identifier;
import prompto.literal.DictLiteral;
import prompto.runtime.Context;
import prompto.runtime.MethodLocator;
import prompto.utils.ResourceUtils;

import com.coveo.nashorn_modules.Folder;
import com.coveo.nashorn_modules.Require;

@SuppressWarnings("restriction")
public class Nashorn8Engine implements IJSEngine {

	public static void executeTests(Context context) throws Exception {
		Transpiler transpiler = new Transpiler(new Nashorn8Engine(), context);
		Collection<TestMethodDeclaration> tests = context.getTests();
		tests.forEach(test->test.declare(transpiler));
		Invocable invocable = transpile(transpiler);
		for(TestMethodDeclaration test : tests)
			invocable.invokeFunction(test.getTranspiledName());
	}
	
	public static void executeMainNoArgs(Context context) throws Exception {
		Transpiler transpiler = new Transpiler(new Nashorn8Engine(), context);
		IMethodDeclaration method = MethodLocator.locateMethod(context, new Identifier("main"), new DictLiteral(false));
		method.declare(transpiler);
		Invocable invocable = transpile(transpiler);
		invocable.invokeFunction("main$Text_dict");
	}
	
	public static Invocable transpile(Transpiler transpiler) throws Exception {
		JSContext.set(transpiler.getContext());
		String js = transpiler.toString();
		try(OutputStream output = new FileOutputStream("transpiled.js")) {
			output.write(js.getBytes());
		}
		List<String> lines = Arrays.asList(
				"var Set = Java.type('" + JSSet.class.getName() + "');",
				"var React = { createElement: function() { return {}; }, Component: function() { this.getInitialState = function() { return {}; }; return this; } };",
				"var ReactBootstrap = { Button: function() { this.render = function() { return {}; }; return this; } };",
				"var process = { stdout: { write: print } };",
				js,
				"var $context = Java.type('" + JSContext.class.getName() + "');"
				);
		js = lines.stream().collect(Collectors.joining("\n"));
		ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
		Require.enable((NashornScriptEngine)nashorn, new ResourceFolder(""));
		nashorn.eval(js);
		Object dataStore = nashorn.getBindings(ScriptContext.ENGINE_SCOPE).get("DataStore");
		if(dataStore instanceof ScriptObjectMirror)
			((ScriptObjectMirror)dataStore).setMember("instance", new MemStoreMirror(nashorn));
		return (Invocable)nashorn;
	}
	
	
	@Override
	public Iterable<String> getPolyfills() {
		return 	Arrays.asList("ObjectAssign", "ObjectIs", "StringRepeat", "StringIncludes", "ArrayFrom", "ArrayIncludes");
	}


	@Override
	public boolean supportsDestructuring() {
		return false;
	}
	
	@Override
	public boolean supportsClass() {
		return false;
	}
	

	static class ResourceFolder implements Folder {

		String path;
		
		public ResourceFolder(String path) {
			this.path = path;
		}

		@Override
		public String getFile(String name) {
			String result = getRealResource(name);
			if(result==null)
				result = getPolyfill(name);
			if(result==null)
				result = getNodeModule(name);
			return result;
		}
		
		private String getPolyfill(String name) {
			return getResourceAsString("polyfills/" + name);
		}

		public String getNodeModule(String name) {
			return getResourceAsString("node_modules/" + name);
		}


		private String getRealResource(String name) {
			String path = this.path.isEmpty() ? name : this.path + "/" + name;
			return getResourceAsString(path);
		}


		public String getResourceAsString(String path) {
			try {
				return ResourceUtils.getResourceAsString(path);
			} catch(Throwable t) {
				return null;
			}
		}

		@Override
		public Folder getFolder(String name) {
			if("node_modules".equals(name))
				return new ResourceFolder("");
			else if(path.isEmpty())
				return new ResourceFolder(name);
			else
				return new ResourceFolder(path + "/" + name);
		}

		@Override
		public Folder getParent() {
			String[] parts = path.split("/");
			if(parts.length==1)
				return null;
			String[] parent = new String[parts.length-1];
			System.arraycopy(parts,  0, parent, 0, parent.length);
			String parentPath = Stream.of(parent).collect(Collectors.joining("/"));
			return new ResourceFolder(parentPath);
		}

		@Override
		public String getPath() {
			return path.isEmpty() ? path : path + "/";
		}
	
	}
	
}
