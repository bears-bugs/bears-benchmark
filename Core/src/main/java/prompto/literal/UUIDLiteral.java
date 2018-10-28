package prompto.literal;

import java.util.UUID;

import prompto.compiler.Flags;
import prompto.compiler.IConstantOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.UUIDType;
import prompto.value.UUIDValue;


public class UUIDLiteral extends Literal<UUIDValue> {

	public UUIDLiteral(String text) {
		super(text, parse(text));
	}

	private static UUIDValue parse(String text) {
		return new UUIDValue(java.util.UUID.fromString(text.substring(1, text.length()-1)));
	}

	@Override
	public IType check(Context context) {
		return UUIDType.instance();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		java.util.UUID uuid = value.getValue();
		IConstantOperand operand = new StringConstant(uuid.toString());
		method.addInstruction(Opcode.LDC_W, operand);
		MethodConstant m = new MethodConstant(UUID.class, "fromString", String.class, UUID.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		return new ResultInfo(UUID.class);
	}

	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("UUID");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("UUID.fromString(").append(this.text.get()).append(")");
		return false;
	}

}
