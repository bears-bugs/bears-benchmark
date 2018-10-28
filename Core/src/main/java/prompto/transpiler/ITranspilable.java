package prompto.transpiler;

@FunctionalInterface
public interface ITranspilable {
	boolean transpile(Transpiler transpiler);
}
