package prompto.literal;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.intrinsic.PromptoDocument;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DocumentType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Document;
import prompto.value.IValue;
import prompto.value.Text;


public class DocumentLiteral extends Literal<Document> {

	// we can only compute keys by evaluating key expressions
	// so we can't just inherit from Map<String,Expression>. 
	// so we keep the full entry list.
	DocEntryList entries;
	
	public DocumentLiteral() {
		super("{}", new Document());
		this.entries = new DocEntryList();
	}
	
	public DocumentLiteral(DocEntryList entries) {
		super(()->entries.toString(), new Document());
		this.entries = entries;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		this.entries.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		return DocumentType.instance(); 
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(entries.size()>0) {
			PromptoDocument<Text,IValue> doc = new PromptoDocument<Text, IValue>();
			for(DictEntry e : entries) {
				Text key = e.getKey().asText();
				IValue val = e.getValue().interpret(context); 
				doc.put(key, val);
			}
			return new Document(context, doc);
		} else
			return value;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = CompilerUtils.compileNewInstance(method, PromptoDocument.class);
		addEntries(context, method, flags.withPrimitive(false));
		return info;
	}

	private void addEntries(Context context, MethodInfo method, Flags flags) {
		for(DictEntry e : entries) {
			method.addInstruction(Opcode.DUP); // need to keep a reference to the map on top of stack
			ResultInfo info = e.getKey().compile(context, method, flags);
			if(info.getType()!=String.class) {
				MethodConstant m = new MethodConstant(info.getType(), "toString", 
						String.class);
				method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			}
			e.getValue().compile(context, method, flags);
			MethodConstant m = new MethodConstant(PromptoDocument.class, "put", 
					Object.class, Object.class, Object.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			method.addInstruction(Opcode.POP); // consume the returned value (null since this is a new Map)
		}
	}

	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("Document");
	    this.entries.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("new Document(");
	    this.entries.transpile(transpiler);
	    transpiler.append(")");
	    return false;
	}
	
}
