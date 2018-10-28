package prompto.argument;

import java.util.Objects;

import prompto.error.SyntaxError;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.type.CodeType;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public class CodeArgument extends BaseArgument implements ITypedArgument {
	
	public CodeArgument(Identifier id) {
		super(id);
	}
	
	@Override
	public IType getType() {
		return CodeType.instance();
	}
	
	@Override
	public String getSignature(Dialect dialect) {
		return CodeType.instance().getTypeName() + " " + id;
	}
	
	@Override
	public String getProto() {
		return CodeType.instance().getTypeName();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(CodeType.instance().getTypeName());
		writer.append(" ");
		writer.append(id);
	}
	
	@Override
	public String toString() {
		return id.toString() + ':' + CodeType.instance().getTypeName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof CodeArgument))
			return false;
		CodeArgument other = (CodeArgument)obj;
		return Objects.equals(this.getId(),other.getId());
	}

	@Override
	public void register(Context context) {
		INamed actual = context.getRegisteredValue(INamed.class, id);
		if(actual!=null)
			throw new SyntaxError("Duplicate argument: \"" + id + "\"");
		context.registerValue(this);
	}
	
	@Override
	public void check(Context context) {
	}
	
	@Override
	public IType getType(Context context) {
		return CodeType.instance();
	}
	
	@Override
	public String getTranspiledName(Context context) {
		return id.toString();
	}

}
