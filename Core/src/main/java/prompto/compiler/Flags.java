package prompto.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prompto.grammar.CmpOp;

public class Flags {

	Map<String, Object> values;
	
	public Flags() {
		this.values = new HashMap<>();
		this.values.put("primitive", false);
		this.values.put("reverse", false);
		this.values.put("roughly", false);
		this.values.put("decimal", false);
		this.values.put("member", false);
		this.values.put("inline", false);
		this.values.put("opcode", null);
		this.values.put("getter", null);
		this.values.put("setter", null);
		this.values.put("variable", null);
		this.values.put("loopListeners", null);
	}
	
	private Flags(Map<String, Object> values) {
		this.values = values;
	}
	
	private Flags clone(String key, Object value) { 
		Map<String, Object> values = new HashMap<>(this.values);
		values.put(key, value);
		return new Flags(values);
	}
	
	public boolean toPrimitive() {
		return (boolean)values.get("primitive");
	}

	public Flags withPrimitive(boolean set) {
		return clone("primitive", set);
	}

	public boolean isReverse() {
		return (boolean)values.get("reverse");
	}

	public Flags withReverse(boolean set) {
		return clone("reverse", set);
	}

	public boolean isDecimal() {
		return (boolean)values.get("decimal");
	}

	public Flags withDecimal(boolean set) {
		return clone("decimal", set);
	}
	
	public Opcode opcode() {
		return (Opcode)values.get("opcode");
	}

	public Flags withOpcode(Opcode opcode) {
		return clone("opcode", opcode);
	}

	public boolean isRoughly() {
		return (boolean)values.get("roughly");
	}

	public Flags withRoughly(boolean set) {
		return clone("roughly", set);
	}

	public CmpOp cmpOp() {
		return (CmpOp)values.get("CmpOp");
	}
	
	public Flags withCmpOp(CmpOp operator) {
		return clone("CmpOp", operator);
	}

	public FieldInfo setter() {
		return (FieldInfo)values.get("setter");
	}
	
	public Flags withSetter(FieldInfo field) {
		return clone("setter", field);
	}
	
	public FieldInfo getter() {
		return (FieldInfo)values.get("getter");
	}
	
	public Flags withGetter(FieldInfo field) {
		return clone("getter", field);
	}
	
	public String variable() {
		return (String)values.get("variable");
	}
	
	public Flags withVariable(String variable) {
		return clone("variable", variable);
	}
	
	public boolean isMember() {
		return (boolean)values.get("member");
	}

	public Flags withMember(boolean set) {
		return clone("member", set);
	}

	public boolean isInline() {
		return (boolean)values.get("inline");
	}

	public Flags withInline(boolean set) {
		return clone("inline", set);
	}

	@SuppressWarnings("unchecked")
	public List<IInstructionListener> getBreakLoopListeners() {
		return (List<IInstructionListener>)values.get("loopListeners");
	}
	
	public Flags withBreakLoopListeners(List<IInstructionListener> listeners) {
		return clone("loopListeners", listeners);
	}
	
	public void addBreakLoopListener(IInstructionListener listener) {
		getBreakLoopListeners().add(listener);
	}


}
