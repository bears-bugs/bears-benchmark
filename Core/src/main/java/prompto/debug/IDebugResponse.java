package prompto.debug;

import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface IDebugResponse {
	
	@JsonIgnore
	Type getType();

	public static class VoidResponse implements IDebugResponse {

		public VoidResponse() {
		}

		@Override
		public Type getType() {
			return Type.VOID;
		}

	}

	public static class GetStatusResponse implements IDebugResponse {

		Status status;
		
		public GetStatusResponse() {
		}

		public GetStatusResponse(Status status) {
			this.status = status;
		}
		
		@Override
		public Type getType() {
			return Type.GET_STATUS;
		}

		public Status getStatus() {
			return status;
		}
		
		public void setStatus(Status status) {
			this.status = status;
		}

	}
	
	public static class GetLineResponse implements IDebugResponse {

		int line;
		
		public GetLineResponse() {
		}

		public GetLineResponse(int line) {
			this.line = line;
		}
		
		@Override
		public Type getType() {
			return Type.GET_LINE;
		}

		public int getLine() {
			return line;
		}
		
		public void setLine(int line) {
			this.line = line;
		}

	}
	
	public static class GetStackResponse implements IDebugResponse {

		LeanStack stack;
		
		public GetStackResponse() {
			this.stack = new LeanStack();
		}

		public GetStackResponse(IStack<?> stack) {
			this.stack = new LeanStack(stack);
		}
		
		@Override
		public Type getType() {
			return Type.GET_STACK;
		}

		public LeanStack getStack() {
			return stack;
		}
		
		public void setStack(LeanStack stack) {
			this.stack = stack;
		}

	}
	
	public static class GetVariablesResponse implements IDebugResponse {

		LeanVariableList variables;
		
		public GetVariablesResponse() {
			this.variables = new LeanVariableList();
		}

		public GetVariablesResponse(Collection<? extends IVariable> variables) {
			this.variables = new LeanVariableList();
			this.variables.addAll(variables.stream()
					.map(LeanVariable::new)
					.collect(Collectors.toList()));
		}
		
		@Override
		public Type getType() {
			return Type.GET_VARIABLES;
		}

		public LeanVariableList getVariables() {
			return variables;
		}
		
		public void setVariables(LeanVariableList variables) {
			this.variables = variables;
		}

	}
	
	public static class IsSteppingResponse implements IDebugResponse {

		boolean stepping;
		
		public IsSteppingResponse() {
		}

		public IsSteppingResponse(boolean stepping) {
			this.stepping = stepping;
		}
		
		@Override
		public Type getType() {
			return Type.IS_STEPPING;
		}

		public boolean isStepping() {
			return stepping;
		}
		
		public void setStepping(boolean stepping) {
			this.stepping = stepping;
		}

	}
	

	public enum Type {
		VOID(VoidResponse.class),
		GET_STATUS(GetStatusResponse.class),
		GET_LINE(GetLineResponse.class),
		GET_STACK(GetStackResponse.class),
		GET_VARIABLES(GetVariablesResponse.class),
		IS_STEPPING(IsSteppingResponse.class)
		;
		
		Class<? extends IDebugResponse> klass;
		
		Type(Class<? extends IDebugResponse> klass) {
			this.klass = klass;
		}
		
		public Class<? extends IDebugResponse> getKlass() {
			return klass;
		}
	}

}
