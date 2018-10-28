package prompto.java;

import java.util.HashMap;
import java.util.Map;

import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.PromptoClassLoader;
import prompto.compiler.ResultInfo;
import prompto.runtime.Context;
import prompto.type.IType;

public abstract class JavaIdentifierProcessor {
	
	public static Map<String, JavaIdentifierProcessor> processors = new HashMap<>();
	
	static {
		processors.put("$context", new ContextIdentifierProcessor());
	}
	
	public abstract IType check(Context context);
	public abstract Object interpret(Context context);
	public abstract ResultInfo compile(Context context, MethodInfo method);
	
	static class ContextIdentifierProcessor extends JavaIdentifierProcessor {
		
		@Override
		public IType check(Context context) {
			return new JavaClassType(context.getClass());
		}
		
		@Override
		public Object interpret(Context context) {
			return context;
		}
		
		@Override
		public ResultInfo compile(Context context, MethodInfo method) {
			// PromptoClassLoader.getInstance().getContext()
			MethodConstant m = new MethodConstant(PromptoClassLoader.class, "getInstance", PromptoClassLoader.class);
			method.addInstruction(Opcode.INVOKESTATIC, m);
			m = new MethodConstant(PromptoClassLoader.class, "getContext", Context.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			return new ResultInfo(Context.class);
		}
	}
	
}
