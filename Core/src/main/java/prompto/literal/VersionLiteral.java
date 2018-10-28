package prompto.literal;

import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.intrinsic.PromptoVersion;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VersionType;
import prompto.value.Version;


public class VersionLiteral extends Literal<Version> {

	public VersionLiteral(String text) {
		super(text,parseVersion(text.substring(2,text.length()-1)));
	}
	
	public VersionLiteral(PromptoVersion version) {
		super("'v" + version.toString() + "'", new Version(version));
	}

	@Override
	public IType check(Context context) {
		return VersionType.instance();
	}
	
	public static Version parseVersion(String text) {
		return new Version(PromptoVersion.parse(text));
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		PromptoVersion version = value.getStorableData();
		method.addInstruction(Opcode.LDC_W, new StringConstant(version.toString()));
		IOperand oper = new MethodConstant(PromptoVersion.class, "parse", String.class, PromptoVersion.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoVersion.class);
	}

	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("Version");

	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("Version.Parse(").append(this.text.get()).append(")");
		return false;
	}
	
}
