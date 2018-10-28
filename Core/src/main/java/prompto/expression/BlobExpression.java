package prompto.expression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.intrinsic.PromptoBinary;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.BlobType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Blob;
import prompto.value.IValue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class BlobExpression implements IExpression {

	IExpression source;
	
	public BlobExpression(IExpression source) {
		this.source = source;
	}
	
	@Override
	public IType check(Context context) {
		source.check(context);
		return BlobType.instance();
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue value = source.interpret(context);
		try {
			Map<String, byte[]> datas = collectData(context, value);
			byte[] zipped = zipData(datas);
			return new Blob("application/zip", zipped);
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo blob = CompilerUtils.compileNewInstance(method, PromptoBinary.class);
		method.addInstruction(Opcode.DUP);
		source.compile(context, method, flags);
		MethodConstant m = new MethodConstant(PromptoBinary.class, "populateFrom", Object.class, void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		return blob;
	}

	private byte[] zipData(Map<String, byte[]> datas) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(output);
		for(Map.Entry<String, byte[]> part : datas.entrySet()) {
			ZipEntry entry = new ZipEntry(part.getKey());
			zip.putNextEntry(entry);
			zip.write(part.getValue());
			zip.closeEntry();
		}
		zip.close();
		return output.toByteArray();
	}

	private Map<String, byte[]> collectData(Context context, IValue value) throws IOException {
		Map<String, byte[]> binaries = new HashMap<>();
		// create textual data
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createGenerator(output);
		value.toJson(context, generator, null, null, true, binaries);
		generator.flush();
		generator.close();
		// add it
		binaries.put("value.json", output.toByteArray());
		return binaries;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("Blob");
		switch(writer.getDialect()) {
		case E:
			writer.append(" from ");
			source.toDialect(writer);
			break;
		case O:
		case M:
			writer.append('(');
			source.toDialect(writer);
			writer.append(')');
			break;
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.source.declare(transpiler);
	    transpiler.require("Blob");
	    transpiler.require("Document");
	    transpiler.require("getUtf8CharLength");
	    transpiler.require("stringToUtf8Buffer");
	    transpiler.require("utf8BufferToString");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("Blob.ofValue(");
	    this.source.transpile(transpiler);
	    transpiler.append(")");
		return false;
	}
}
