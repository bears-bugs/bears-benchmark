package prompto.statement;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

/* make comment a statement to fit in the general toDialect mechanism */
public class CommentStatement extends BaseStatement {
	
	String text;
	
	public CommentStatement(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}

	@Override
	public IType check(Context context) {
		return VoidType.instance();
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		return null;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		String[] lines = text.split("\n");
		for(int i=0;i<lines.length;i++)
			lines[i] = uncomment(lines[i]);
		switch(writer.getDialect()) {
		case E:
		case O:
			if(lines.length>1) {
				writer.append("/*");
				for(String line : lines) {
					writer.append(line);
					writer.newLine();
				}
				writer.trimLast(1);
				writer.append("*/");
				writer.newLine();
			} else {
				writer.append("//");
				writer.append(lines[0]);
				writer.newLine();
			}
			break;
		case M:	
			for(String line : lines) {
				writer.append("#");
				writer.append(line);
				writer.newLine();
			}
			break;
		}		
	}

	private static String uncomment(String line) {
		if(line.startsWith("#"))
			return line.substring(1);
		else if(line.startsWith("//"))
			return line.substring(2);
		else
			return line;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		// nathing to do
		return new ResultInfo(void.class);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		return true;
	}
}
