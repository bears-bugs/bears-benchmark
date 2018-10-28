package prompto.argument;

import java.util.Objects;

import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.utils.CodeWriter;

public class CategoryArgument extends BaseArgument implements ITypedArgument {
	
	IType type;
	
	public CategoryArgument(IType type, Identifier id) {
		super(id);
		this.type = type;
	}

	public CategoryArgument(IType type, Identifier id, IExpression defaultValue) {
		super(id);
		this.type = type;
		setDefaultExpression(defaultValue);
	}

	@Override
	public IType getType() {
		return type;
	}
	
	@Override
	public String getSignature(Dialect dialect) {
		return getProto();
	}
	
	@Override
	public String getProto() {
		return type.getTypeNameId().toString();
	}
	
	@Override
	public String getTranspiledName(Context context) {
		return type.getTranspiledName(context);
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(mutable)
			writer.append("mutable ");
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
		if(defaultExpression!=null) {
			writer.append(" = ");
			defaultExpression.toDialect(writer);
		}
	}
	
	private void toEDialect(CodeWriter writer) {
		boolean anonymous = "any".equals(type.getTypeName());
		type.toDialect(writer);
		if(anonymous) {
			writer.append(' ');
			writer.append(id);
		}
		if(!anonymous) {
			writer.append(' ');
			writer.append(id);
		}
	}

	private void toODialect(CodeWriter writer) {
		type.toDialect(writer);
		writer.append(' ');
		writer.append(id);
	}

	private void toMDialect(CodeWriter writer) {
		writer.append(id);
		writer.append(':');
		type.toDialect(writer);
	}

	@Override
	public String toString() {
		return id.toString() + ':' + getProto();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof CategoryArgument))
			return false;
		CategoryArgument other = (CategoryArgument)obj;
		return Objects.equals(this.getType(),other.getType())
				&& Objects.equals(this.getId(),other.getId());
	}

	@Override
	public void register(Context context) {
		INamed actual = context.getRegisteredValue(INamed.class, id);
		if(actual!=null)
			throw new SyntaxError("Duplicate argument: \"" + id + "\"");
		context.registerValue(this);
		if(defaultExpression!=null) try {
			context.setValue(id, defaultExpression.interpret(context));
		} catch(PromptoError error) {
			throw new SyntaxError("Unable to register default value: "+ defaultExpression.toString() + " for argument: " + id);
		}
	}
	
	@Override
	public void check(Context context) {
		type.checkExists(context);
	}
	
	@Override
	public IType getType(Context context) {
		return type;
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.type.declare(transpiler);
	}
	
	@Override
	public void transpileCall(Transpiler transpiler, IExpression expression) {
		IType expType = expression.check(transpiler.getContext());
	    if (this.type == IntegerType.instance() && expType == DecimalType.instance()) {
	        transpiler.append("Math.round(");
	        expression.transpile(transpiler);
	        transpiler.append(")");
	    } else
	    	expression.transpile(transpiler);
    }	
}
