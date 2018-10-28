package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.problem.IProblemListener;
import prompto.problem.ProblemListener;
import prompto.runtime.Context;
import prompto.statement.UnresolvedCall;
import prompto.transpiler.Transpiler;
import prompto.type.AnyType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class UnresolvedSelector extends SelectorExpression {

	Identifier id;
	IExpression resolved;

	public UnresolvedSelector(Identifier name) {
		this.id = name;
	}

	public IExpression getResolved() {
		return resolved;
	}

	public Identifier getId() {
		return id;
	}

	public String getName() {
		return id.toString();
	}

	@Override
	public String toString() {
		return (parent == null ? "" : parent.toString() + '.') + id.toString();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		try {
			resolve(writer.getContext(), false);
		} catch (SyntaxError e) {
		}
		if (resolved != null)
			resolved.toDialect(writer);
		else {
			if (parent != null) {
				parent.toDialect(writer);
				writer.append('.');
			}
			writer.append(id);
		}
	}

	@Override
	public IType check(Context context) {
		return resolveAndCheck(context, false);
	}

	public IType checkMember(Context context) {
		return resolveAndCheck(context, true);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		resolveAndCheck(context, false);
		return resolved.interpret(context);
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		resolveAndCheck(context, false);
		return resolved.compile(context, method, flags);
	}

	private IType resolveAndCheck(Context context, boolean forMember) {
		resolve(context, forMember);
		return resolved != null ? resolved.check(context) : AnyType.instance();
	}

	public IExpression resolve(Context context, boolean forMember) {
		if (resolved == null) {
			IProblemListener saved = context.getProblemListener();
			try {
				context.setProblemListener(new ProblemListener() {
					@Override
					public boolean isCheckNative() {
						return saved.isCheckNative();
					}
				});
				resolved = resolveMethod(context);
				if (resolved == null)
					resolved = resolveMember(context);
			} finally {
				context.setProblemListener(saved);
			}
			if (resolved == null)
				context.getProblemListener().reportUnknownIdentifier(id.toString(), this);
		}
		return resolved;
	}

	private IExpression resolveMember(Context context) {
		try {
			MemberSelector member = new MemberSelector(parent, id);
			member.setFrom(this);
			member.check(context);
			return member;
		} catch (SyntaxError e) {
			// ignore resolution errors
			return null;
		}
	}

	private IExpression resolveMethod(Context context) {
		try {
			IExpression resolvedParent = parent;
			if (resolvedParent instanceof UnresolvedIdentifier) {
				((UnresolvedIdentifier) resolvedParent).checkMember(context);
				resolvedParent = ((UnresolvedIdentifier) resolvedParent).getResolved();
			}
			UnresolvedCall method = new UnresolvedCall(new MethodSelector(resolvedParent, id), null);
			method.setFrom(this);
			method.check(context);
			return method;
		} catch (SyntaxError e) {
			// ignore resolution errors
			return null;
		}
	}

	@Override
	public void declare(Transpiler transpiler) {
		if (this.resolved == null)
			this.resolve(transpiler.getContext(), false);
		this.resolved.declare(transpiler);
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
		if (this.resolved == null)
			this.resolve(transpiler.getContext(), false);
		return this.resolved.transpile(transpiler);
	}

}
