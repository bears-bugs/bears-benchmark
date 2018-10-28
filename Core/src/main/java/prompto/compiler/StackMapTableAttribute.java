package prompto.compiler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StackMapTableAttribute implements IAttribute {

	Utf8Constant attributeName = new Utf8Constant("StackMapTable");
	StackState state = new StackState();
	List<StackLabel> labels = new ArrayList<>();
	short maxStackSize = 0;
	short maxLocalsCount = 0;
	
	public StackState getState() {
		return state;
	}
	
	public short getMaxStack() {
		return maxStackSize;
	}

	public short getMaxLocals() {
		return (short)(1 + maxLocalsCount);
	}

	public void addLabel(StackLabel label) {
		labels.add(label);
	}

	public void push(StackEntry ... entries) {
		for(StackEntry e : entries)
			push(e);
	}
	
	public StackEntry push(StackEntry item) {
		StackEntry result = state.pushEntry(item);
		if(state.getCurrentSize()>maxStackSize) {
			if(DumpLevel.current()==DumpLevel.STACK)
				System.err.print("maxStackSize " + maxStackSize);
			maxStackSize = state.getCurrentSize();
			if(DumpLevel.current()==DumpLevel.STACK)
				System.err.println(" -> " + maxStackSize);
		}
		return result;
	};
	
	public StackEntry[] pop(short popped) {
		StackEntry[] result = new StackEntry[popped];
		while(popped-->0)
			result[popped] = popEntry();
		return result;
	}
	
	public StackEntry popEntry() {
		return state.popEntry();
	}

	private int labelsLength() {
		return (int)labels.stream().mapToInt((l)->l.length()).sum(); 
	}

	@Override
	public void register(ConstantsPool pool) {
		attributeName.register(pool);
		state.register(pool);
		labels.forEach((l)->
			l.register(pool));
	}

	public StackLocal pushLocal(StackLocal local) {
		if(local.getIndex()>=maxLocalsCount)
			maxLocalsCount = (short)(1 + local.getIndex());
		state.pushLocal(local);
		return local;
	}
	
	public StackLocal popLocal(StackLocal local) {
		if(local!=state.peekLocal())
			throw new UnsupportedOperationException();
		return state.popLocal();
	}


	@Override
	public int lengthWithoutHeader() {
		/*
		StackMapTable_attribute {
		    u2              attribute_name_index;
		    u4              attribute_length;
		    u2              number_of_entries;
		    stack_map_frame entries[number_of_entries];
		}
		*/
		cleanupAndOptimize();
		return 2 + labelsLength();
	}
	
	private void cleanupAndOptimize() {
		removeRedundantLabels();
		// TODO convert FULL to SAME etc...
	}

	private void removeRedundantLabels() {
		int lastRealOffset = -1;
		List<StackLabel> labels = new ArrayList<>();
		for(StackLabel label : this.labels) {
			if(lastRealOffset!=label.getRealOffset())
				labels.add(label);
			lastRealOffset = label.getRealOffset();
		};
		this.labels = labels;
	}

	@Override
	public void writeTo(ByteWriter writer) {
		/*
		StackMapTable_attribute {
		    u2              attribute_name_index;
		    u4              attribute_length;
		    u2              number_of_entries;
		    stack_map_frame entries[number_of_entries];
		}
		*/
		writer.writeU2(attributeName.getIndexInConstantPool());
		writer.writeU4(lengthWithoutHeader());
		writer.writeU2((short)labels.size());
		int lastRealOffset = 0;
		for(StackLabel label : labels) {
			int deltaOffset = label.getRealOffset()-
					(lastRealOffset==0 ? lastRealOffset : lastRealOffset + 1); // see java class file format
			label.setDeltaOffset(deltaOffset);
			lastRealOffset = label.getRealOffset();
		}
		labels.forEach((l)->
			l.writeTo(writer));
	}

	public byte[] getData() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteWriter writer = new ByteWriter(output);
		writeTo(writer);
		return output.toByteArray();
	}


}
