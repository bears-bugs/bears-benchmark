package prompto.java;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.runtime.Context;
import prompto.runtime.VoidResult;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;
import prompto.value.NullValue;

public class JavaStatement {

	JavaExpression expression;
	boolean isReturn;
	
	public JavaStatement(JavaExpression expression,boolean isReturn) {
		this.expression = expression;
		this.isReturn = isReturn;
	}

	public IType check(Context context, IType returnType) {
		IType type = expression.check(context);
		if(type instanceof JavaClassType) 
			type = ((JavaClassType)type).convertJavaClassToPromptoType(context, returnType);
		return isReturn ? type : VoidType.instance();
	}

	public IValue interpret(Context context, IType returnType) throws PromptoError {
		Object result = expression.interpret(context);
		if(result==null) {
			if(isReturn) {
				if(returnType==null)
					return VoidResult.instance();
				else
					return NullValue.instance();
			} else 
				return null;
		} else {	
            IType type = expression.check(context);
            if (type instanceof JavaClassType)
                return ((JavaClassType)type).convertJavaValueToPromptoValue(context, result, returnType);
            else
            	// TODO warning or exception?
            	return VoidResult.instance();
		}
	}
	
	@Override
	public String toString() {
		return "" + (isReturn ? "return " : "") + expression.toString() + ";";
	}

	public void toDialect(CodeWriter writer) {
		if(isReturn)
			writer.append("return ");
		expression.toDialect(writer);
		writer.append(';');
	}

	static Map<Class<?>, Function<MethodInfo, ResultInfo>> resultConverters = createResultConverters();
	static Map<Class<?>, Function<MethodInfo, ResultInfo>> resultConsumers = createResultConsumers();
	
	private static Map<Class<?>, Function<MethodInfo, ResultInfo>> createResultConverters() {
		Map<Class<?>, Function<MethodInfo, ResultInfo>> map = new HashMap<>();
		map.put(boolean.class, CompilerUtils::booleanToBoolean);
		map.put(byte.class, CompilerUtils::intTolong); // no byte in the JVM
		map.put(Byte.class, CompilerUtils::ByteToLong);
		map.put(short.class, CompilerUtils::intToLong); // no short in the JVM
		map.put(short.class, CompilerUtils::ShortToLong);
		map.put(int.class, CompilerUtils::intToLong);
		map.put(Integer.class, CompilerUtils::IntegerToLong);
		map.put(long.class, CompilerUtils::longToLong);
		map.put(float.class, CompilerUtils::floatToDouble);
		map.put(Float.class, CompilerUtils::FloatToDouble);
		map.put(double.class, CompilerUtils::doubleToDouble);
		map.put(char.class, CompilerUtils::charToCharacter);
		return map;
	}
	
	private static Map<Class<?>, Function<MethodInfo, ResultInfo>> createResultConsumers() {
		Map<Class<?>, Function<MethodInfo, ResultInfo>> map = new HashMap<>();
		return map;
	}

	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = expression.compile(context, method);
		if(isReturn) {
			if(info.getType()==void.class)
				throw new SyntaxError("Cannot return void!"); // TODO add a test to ensure this has been caught earlier
			// convert native type to object type
			Function<MethodInfo, ResultInfo> converter = resultConverters.get(info.getType());
			if(converter!=null)
				info = converter.apply(method);
			if(!flags.isInline())
				method.addInstruction(Opcode.ARETURN);
			return info;
		} else if(info.getType()!=void.class) {
			Function<MethodInfo, ResultInfo> consumer = resultConsumers.get(info.getType());
			if(consumer!=null)
				info = consumer.apply(method);
			else
				method.addInstruction(Opcode.POP);
			return new ResultInfo(void.class);
		} else
			return info;
	}


}
