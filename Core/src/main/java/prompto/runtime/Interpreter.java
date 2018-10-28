package prompto.runtime;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import prompto.argument.UnresolvedArgument;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.expression.MethodSelector;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoDict;
import prompto.literal.DictLiteral;
import prompto.statement.MethodCall;
import prompto.type.DictType;
import prompto.type.IType;
import prompto.type.TextType;
import prompto.utils.CmdLineParser;
import prompto.value.Dictionary;
import prompto.value.ExpressionValue;
import prompto.value.IValue;
import prompto.value.Text;

public class Interpreter {
	
	static IType argsType = new DictType(TextType.instance());
			
	private Interpreter() {
	}
	
	public static void interpretTests(Context context) throws PromptoError {
		Collection<TestMethodDeclaration> tests = context.getTests();
		for(TestMethodDeclaration test : tests) {
			Context local = context.newLocalContext();
			test.interpret(local);
		}
		
	}

	public static void interpretTest(Context context, Identifier testName, boolean lookInStore) throws PromptoError {
		TestMethodDeclaration test = context.getTest(testName, lookInStore);
		Context local = context.newLocalContext();
		test.interpret(local);
	}
	
	public static void interpretMain(Context context, Map<String, String> cmdLineArgs) throws PromptoError {
		try {
			IExpression args = convertCmdLineArgs(cmdLineArgs);
			interpretMethod(context, new Identifier("main"), args);
		} finally {
			context.notifyTerminated();
		}
	}
	
	public static void interpretMainNoArgs(Context context, Identifier name) throws PromptoError {
		interpretMethod(context, name, "");
	}
	
	public static void interpretMainNoArgs(Context context) throws PromptoError {
		interpretMethod(context, new Identifier("main"), "");
	}


	public static void interpretMethod(Context context, Identifier methodName, String cmdLineArgs) throws PromptoError {
		try {
			IExpression args = parseCmdLineArgs(cmdLineArgs);
			interpretMethod(context, methodName, args);
		} finally {
			context.notifyTerminated();
		}
	}
	
	public static void interpretMethod(Context context, Identifier methodName, IExpression args) {
		IMethodDeclaration method = MethodLocator.locateMethod(context, methodName, args);
		ArgumentAssignmentList assignments = buildAssignments(method, args);
		MethodCall call = new MethodCall(new MethodSelector(methodName), assignments);
		call.interpret(context);	
	}

	public static void interpretScript(Context context, String cmdLineArgs) throws PromptoError {
		throw new UnsupportedOperationException("yet!");
	}

	public static ArgumentAssignmentList buildAssignments(IMethodDeclaration method, IExpression args) {
		ArgumentAssignmentList assignments = new ArgumentAssignmentList();
		if(method.getArguments().size()==1) {
			Identifier name = method.getArguments().getFirst().getId();
			assignments.add(new ArgumentAssignment(new UnresolvedArgument(name), args)); 
		}
		return assignments;
	}

	public static IExpression parseCmdLineArgs(String cmdLineArgs) {
		try {
			Map<String,String> args = CmdLineParser.parse(cmdLineArgs);
			return convertCmdLineArgs(args);
		} catch(Exception e) {
			// TODO
			return new DictLiteral(false);
		}
	}

	private static IExpression convertCmdLineArgs(Map<String, String> args) {
		PromptoDict<Text, IValue> valueArgs = new PromptoDict<Text, IValue>(true);
		for(Entry<String,String> entry : args.entrySet())
			valueArgs.put(new Text(entry.getKey()), new Text(entry.getValue()));
		valueArgs.setMutable(false);
		Dictionary dict = new Dictionary(TextType.instance(), valueArgs);
		return new ExpressionValue(argsType, dict);
	}

	
	
	
}
