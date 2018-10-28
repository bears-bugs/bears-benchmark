package prompto.constraint;

import prompto.parser.Section;
import prompto.transpiler.ITranspilable;
import prompto.transpiler.Transpiler;

public abstract class MatchingConstraintBase extends Section implements IAttributeConstraint {

	ITranspilable transpileFunction;

	@Override
	public boolean transpile(Transpiler transpiler) {
		return transpileFunction.transpile(transpiler);
	}
}
