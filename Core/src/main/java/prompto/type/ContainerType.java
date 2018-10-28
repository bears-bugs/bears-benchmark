package prompto.type;

import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.store.Family;
import prompto.transpiler.Transpiler;

public abstract class ContainerType extends IterableType {

	protected ContainerType(Family family, IType itemType, String fullName) {
		super(family, itemType, fullName);
	}
	
	@Override
	public IType checkContains(Context context, IType other) {
		if(itemType.isAssignableFrom(context, other))
			return BooleanType.instance();
		else
			return super.checkContains(context, other);
	}
	
	@Override
	public void declareMember(Transpiler transpiler, String name) {
	    if (!"count".equals(name)) {
	        super.declareMember(transpiler, name);
	    }
	}
	
	@Override
	public void transpileMember(Transpiler transpiler, String name) {
	    if ("count".equals(name)) {
	        transpiler.append("length");
	    } else {
	        super.transpileMember(transpiler, name);
	    }
	}
	
	@Override
	public void declareIterator(Transpiler transpiler, Identifier id, IExpression expression) {
	    transpiler = transpiler.newChildTranspiler(null);
	    transpiler.getContext().registerValue(new Variable(id, this.itemType));
	    expression.declare(transpiler);
	}
	
	@Override
	public void transpileIterator(Transpiler transpiler, Identifier id, IExpression expression) {
	    transpiler.append(".iterate(function(").append(id.toString()).append(") { return ");
	    transpiler = transpiler.newChildTranspiler(null);
	    transpiler.getContext().registerValue(new Variable(id, this.itemType));
	    expression.transpile(transpiler);
	    transpiler.append("; }, this)");
	    transpiler.flush();
	}
}
