package prompto.debug;

import static prompto.debug.IDebugResponse.*;
import static prompto.debug.IDebugRequest.Type.*;

import java.util.Collection;

import prompto.parser.ISection;
import prompto.parser.Section;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IDebugRequest {

	@JsonIgnore
	Type getType();
	IDebugResponse execute(IDebugger debugger);

	
	public static class GetStatusRequest implements IDebugRequest {

		public GetStatusRequest() {
		}

		public GetStatusRequest(IThread thread) {
		}

		@Override
		public GetStatusResponse execute(IDebugger debugger) {
			Status status = debugger.getStatus(null);
			return new GetStatusResponse(status);
		}
		
		@Override
		public Type getType() {
			return GET_STATUS;
		}
	}
	
	public static class GetLineRequest implements IDebugRequest {

		public GetLineRequest() {
		}

		public GetLineRequest(IThread thread) {
		}

		@Override
		public GetLineResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before line");
			int line = debugger.getLine(null);
			LocalDebugger.showEvent("after line:" + line);
			return new GetLineResponse(line);
		}
		
		@Override
		public Type getType() {
			return GET_LINE;
		}
	}

	public static class GetStackRequest implements IDebugRequest {

		public GetStackRequest() {
		}

		public GetStackRequest(IThread thread) {
		}

		@Override
		public GetStackResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before stack");
			IStack<?> stack = debugger.getStack(null);
			LocalDebugger.showEvent("after stack");
			return new GetStackResponse(stack);
		}
		
		@Override
		public Type getType() {
			return GET_STACK;
		}
	}

	public static class GetVariablesRequest implements IDebugRequest {

		LeanStackFrame frame;
		
		public GetVariablesRequest() {
		}

		public GetVariablesRequest(IThread thread, IStackFrame frame) {
			this.frame = new LeanStackFrame(frame);
		}

		public void setFrame(LeanStackFrame frame) {
			this.frame = frame;
		}
		
		public LeanStackFrame getFrame() {
			return frame;
		}

		@Override
		public GetVariablesResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before variables");
			Collection<? extends IVariable> variables = debugger.getVariables(null, frame);
			LocalDebugger.showEvent("after variables");
			return new GetVariablesResponse(variables);
		}
		
		@Override
		public Type getType() {
			return GET_VARIABLES;
		}
	}

	public static class InstallBreakpointRequest implements IDebugRequest {
		
		Section section;
		
		public InstallBreakpointRequest() {
		}
		
		public InstallBreakpointRequest(ISection section) {
			this.section = section.getClass()==Section.class ? (Section)section : new Section(section);
		}
		
		public Section getSection() {
			return section;
		}
		
		public void setSection(Section section) {
			this.section = section;
		}

		@Override
		public IDebugResponse execute(IDebugger debugger) {
			debugger.installBreakpoint(section);
			return new VoidResponse();
		}
		
		@Override
		public Type getType() {
			return INSTALL_BREAKPOINT;
		}
	}
	
	
	public static class SuspendRequest implements IDebugRequest {

		public SuspendRequest() {
		}

		public SuspendRequest(IThread thread) {
		}

		@Override
		public VoidResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before suspend");
			debugger.suspend(null);
			LocalDebugger.showEvent("after suspend");
			return new VoidResponse();
		}
		
		@Override
		public Type getType() {
			return SUSPEND;
		}
	}

	public static class ResumeRequest implements IDebugRequest {

		public ResumeRequest() {
		}

		public ResumeRequest(IThread thread) {
		}

		@Override
		public VoidResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before resume");
			debugger.resume(null);
			LocalDebugger.showEvent("after resume");
			return new VoidResponse();
		}
		
		@Override
		public Type getType() {
			return RESUME;
		}
	}

	public static class IsSteppingRequest implements IDebugRequest {

		public IsSteppingRequest() {
		}

		public IsSteppingRequest(IThread thread) {
		}

		@Override
		public IsSteppingResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before is stepping");
			boolean stepping = debugger.isStepping(null);
			LocalDebugger.showEvent("after is stepping");
			return new IsSteppingResponse(stepping);
		}
		
		@Override
		public Type getType() {
			return IS_STEPPING;
		}
	}

	public static class StepOverRequest implements IDebugRequest {

		@Override
		public VoidResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before step over");
			debugger.stepOver(null);
			LocalDebugger.showEvent("after step over");
			return new VoidResponse();
		}
		
		@Override
		public Type getType() {
			return STEP_OVER;
		}
	}

	public static class StepIntoRequest implements IDebugRequest {

		public StepIntoRequest() {
		}

		public StepIntoRequest(IThread thread) {
		}

		@Override
		public VoidResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before step into");
			debugger.stepInto(null);
			LocalDebugger.showEvent("after step into");
			return new VoidResponse();
		}
		
		@Override
		public Type getType() {
			return STEP_INTO;
		}
	}

	public static class StepOutRequest implements IDebugRequest {

		public StepOutRequest() {
		}

		public StepOutRequest(IThread thread) {
		}

		@Override
		public VoidResponse execute(IDebugger debugger) {
			LocalDebugger.showEvent("before step out");
			debugger.stepOut(null);
			LocalDebugger.showEvent("after step out");
			return new VoidResponse();
		}
		
		@Override
		public Type getType() {
			return STEP_OUT;
		}
	}

	public enum Type {
		GET_STATUS(GetStatusRequest.class),
		GET_LINE(GetLineRequest.class),
		GET_STACK(GetStackRequest.class),
		GET_VARIABLES(GetVariablesRequest.class),
		INSTALL_BREAKPOINT(InstallBreakpointRequest.class),
		SUSPEND(SuspendRequest.class),
		RESUME(ResumeRequest.class),
		IS_STEPPING(IsSteppingRequest.class),
		STEP_INTO(StepIntoRequest.class),
		STEP_OUT(StepOutRequest.class),
		STEP_OVER(StepOverRequest.class)
		;
		
		Class<? extends IDebugRequest> klass;
		
		Type(Class<? extends IDebugRequest> klass) {
			this.klass = klass;
		}
		
		public Class<? extends IDebugRequest> getKlass() {
			return klass;
		}
	}

}
