package prompto.compiler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import prompto.grammar.ArgumentList;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.type.VoidType;

public class InterfaceType {
	
	ArgumentList arguments;
	IType returnType;

	public InterfaceType(ArgumentList arguments, IType returnType) {
		this.arguments = arguments;
		this.returnType = returnType;
	}
	
	public boolean isVoid() {
		return returnType==VoidType.instance();
	}


	public Type getInterfaceType() {
		if(isVoid()) switch(arguments.size()) {
		case 0:
			return Runnable.class;
		case 1: 
			return Consumer.class;
		case 2:
			return BiConsumer.class;
		default:
			throw new UnsupportedOperationException("getConsumerType " + arguments.size());
		} else switch(arguments.size()) {
		case 0:
			return Supplier.class;
		case 1: 
			return Function.class;
		case 2:
			return BiFunction.class;
		default:
			throw new UnsupportedOperationException("getSupplierType " + arguments.size());
		}
	}



	public String getInterfaceMethodName() {
		if(isVoid()) switch(arguments.size()) {
		case 0:
			return "run"; // Runnable.class;
		default: 
			return "accept"; // xxConsumer.class;
		} else switch(arguments.size()) {
		case 0:
			return "get"; // Supplier.class;
		default: 
			return "apply"; // Function.class;
		}
	}

	public SignatureAttribute computeSignature(Context context, Type superClass) {
		String scs = CompilerUtils.getDescriptor(superClass);
		Stream<IType> types = arguments.stream()
				.map(arg->arg.getType(context));
		if(!isVoid())
			types = Stream.concat(types, Stream.of(returnType));
		List<Type> javaTypes = types
				.map(t->t.getJavaType(context))
				.collect(Collectors.toList());
		String sis = CompilerUtils.getGenericDescriptor(getInterfaceType(), javaTypes);
		return new SignatureAttribute(scs + sis);
	}







}
