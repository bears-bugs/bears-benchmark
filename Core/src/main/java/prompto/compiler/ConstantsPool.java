package prompto.compiler;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class ConstantsPool extends LinkedList<IConstantOperand> {

	int nextIndex = 1; // 1 based index
	
	int registerConstant(IConstantOperand c) {
		int idx = indexOf(c);
		if(idx>=0)
			return get(idx).getIndexInConstantPool();
		else {
			add(c);
			idx = nextIndex; 
			nextIndex += c.size();
			return idx;
		}
	}

	public void write(ByteWriter writer) throws CompilerException {
		writer.writeU2(nextIndex);
		this.forEach((c) -> 
			c.writeTo(writer));
	}

	public IConstantOperand constantWithIndex(int index) {
		for(IConstantOperand operand : this) {
			if(operand.getIndexInConstantPool()==index)
				return operand;
		}
		return null;
	}

}
