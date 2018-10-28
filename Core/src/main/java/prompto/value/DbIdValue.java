package prompto.value;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonGenerator;

import prompto.error.NotStorableError;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.type.AnyType;
import prompto.type.IType;

public class DbIdValue implements IValue {

	Object dbId;

	public DbIdValue(Object dbId) {
		this.dbId = dbId;
	}

	public void setDbId(Object dbId) {
		this.dbId = dbId;
	}
	
	public Object getDbId() {
		return dbId;
	}
	
	@Override
	public String toString() {
		return dbId.toString();
	}
	
	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> binaries) throws PromptoError {
		try {
			generator.writeString(dbId.toString());
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}
	
	@Override
	public IType getType() {
		return AnyType.instance();
	}

	@Override
	public void setType(IType type) {
		if(type!=AnyType.instance())
			throw new RuntimeException("Should never get there!");
	}
	
	@Override
	public Object getStorableData() throws NotStorableError {
		return dbId;
	}
	
	@Override
	public void collectStorables(Consumer<IStorable> collector) throws NotStorableError {
		// nothing to do
	}


}
