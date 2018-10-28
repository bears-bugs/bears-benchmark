package prompto.debug;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/* designed to be serialized in JSON */
public class LeanStackFrame implements IStackFrame {
	
	String methodName;
	String filePath;
	int index;
	int line;
	int startCharIndex;
	int endCharIndex;
	boolean hasVariables;
	
	public LeanStackFrame() {
	}
	
	public LeanStackFrame(IStackFrame frame) {
		this.methodName = frame.getMethodName();
		this.filePath = frame.getFilePath();
		this.index = frame.getIndex();
		this.line = frame.getLine();
		this.startCharIndex = frame.getStartCharIndex();
		this.endCharIndex = frame.getEndCharIndex();
		this.hasVariables = frame.hasVariables();
	}

	@Override
	public String getMethodName() {
		return methodName;
	}
	
	@Override
	public String getFilePath() {
		return filePath;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	@Override
	public int getEndCharIndex() {
		return endCharIndex;
	}
	
	@Override
	public int getStartCharIndex() {
		return startCharIndex;
	}
	
	@Override
	public boolean hasVariables() {
		return hasVariables;
	}
	
	@JsonIgnore
	@Override
	public Collection<? extends IVariable> getVariables() {
		throw new UnsupportedOperationException("Must override!");
	}
	
	@Override
	public String toString() {
		return methodName + ", line " + Integer.toString(line);
	}
	
	public String toJson() {
		return new ObjectMapper().valueToTree(this).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(!(obj instanceof IStackFrame))
			return false;
		IStackFrame sf = (IStackFrame)obj;
		return this.index==sf.getIndex();
	}

}
