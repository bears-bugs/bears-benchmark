package prompto.literal;

import java.util.List;
import java.util.stream.Collectors;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.intrinsic.PromptoDict;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DictType;
import prompto.type.IType;
import prompto.type.MissingType;
import prompto.utils.CodeWriter;
import prompto.utils.TypeUtils;
import prompto.value.Dictionary;
import prompto.value.IValue;
import prompto.value.Text;


public class DictLiteral extends Literal<Dictionary> {

	// we can only compute keys by evaluating key expressions
	// so we can't just inherit from Map<String,Expression>. 
	// so we keep the full entry list.
	boolean mutable;
	DictEntryList entries;
	IType itemType = null;
	
	public DictLiteral(boolean mutable) {
		super("<:>",new Dictionary(MissingType.instance(), mutable));
		this.entries = new DictEntryList();
		this.mutable = mutable;
	}
	
	public DictLiteral(DictEntryList entries, boolean mutable) {
		super(()->entries.toString(), new Dictionary(MissingType.instance(), mutable));
		this.entries = entries;
		this.mutable = mutable;
	}

	public boolean isMutable() {
		return mutable;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(mutable)
			writer.append("mutable ");
		this.entries.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		if(itemType==null)
			itemType = inferElementType(context);
		return new DictType(itemType); 
	}
	
	// can't use Utils.inferElementType with list of DictEntry
	private IType inferElementType(Context context) {
		if(entries.isEmpty())
			return MissingType.instance();
		List<IType> types = entries.stream().map(e->e.getValue().check(context)).collect(Collectors.toList());
		return TypeUtils.inferCollectionType(context, types);
	}	
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(entries.size()>0) {
			check(context); // to compute itemType
			PromptoDict<Text,IValue> dict = new PromptoDict<Text, IValue>(true);
			for(DictEntry e : entries) {
				Text key = e.getKey().asText();
				IValue val = e.getValue().interpret(context); 
				dict.put(key, val);
			}
			dict.setMutable(mutable);
			return new Dictionary(itemType, dict);
		} else
			return value;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = CompilerUtils.compileNewRawInstance(method, PromptoDict.class);
		method.addInstruction(Opcode.DUP);
		method.addInstruction(Opcode.ICONST_1);
		CompilerUtils.compileCallConstructor(method, PromptoDict.class, boolean.class);
		addEntries(context, method, flags.withPrimitive(false));
		if(!mutable) {
			method.addInstruction(Opcode.DUP);
			method.addInstruction(Opcode.ICONST_0);
			MethodConstant m = new MethodConstant(PromptoDict.class, "setMutable", boolean.class, void.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		}
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
			MethodConstant m = new MethodConstant(PromptoDict.class, "put", 
					Object.class, Object.class, Object.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			method.addInstruction(Opcode.POP); // consume the returned value (null since this is a new Map)
		}
	}

	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("Dictionary");
	    this.entries.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("new Dictionary(").append(this.mutable).append(", ");
	    this.entries.transpile(transpiler);
	    transpiler.append(")");
	    return false;
	}
	
}
