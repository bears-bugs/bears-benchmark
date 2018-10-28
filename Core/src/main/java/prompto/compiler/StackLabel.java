package prompto.compiler;


public abstract class StackLabel {

	int realOffset = -1;
	int deltaOffset = -1;

	public void setRealOffset(int offset) {
		if(DumpLevel.current()==DumpLevel.STACK)
			System.err.println("Setting state real offset " + this.toString() + " to " + offset);
		this.realOffset = offset;
	}

	public int getRealOffset() {
		return realOffset;
	}
	
	public void setDeltaOffset(int offset) {
		if(DumpLevel.current()==DumpLevel.STACK)
			System.err.println("Setting state delta offset " + this.toString() + " to " + offset);
		this.deltaOffset = offset;
	}
	
	public int getDeltaOffset() {
		return deltaOffset;
	}
	
	public abstract int length();
	public abstract void writeTo(ByteWriter writer);

	public void register(ConstantsPool pool) {
		// by default nothing to do
	}
	
	
	public static class SAME extends StackLabel {
		
		@Override
		public int length() {
			if(deltaOffset==-1)
				throw new UnsupportedOperationException();
			return deltaOffset<64 ? 1 : 3;
		}
		
		@Override
		public void writeTo(ByteWriter writer) {
			if(deltaOffset==-1)
				throw new UnsupportedOperationException();
			if(deltaOffset<64)
				writeSAME(writer);
			else
				writeSAME_Extended(writer);
		}

		private void writeSAME_Extended(ByteWriter writer) {
			/*
			same_frame_extended {
			    u1 frame_type = SAME_FRAME_EXTENDED; // 251 
			    u2 offset_delta;
			}
			*/		
			writer.writeU1(251);
			writer.writeU2(deltaOffset);
		}

		private void writeSAME(ByteWriter writer) {
			/*
			same_frame {
		    u1 frame_type = SAME; // 0-63 
			*/
			writer.writeU1(deltaOffset);
		}
	}
	
	public static class FULL extends StackLabel {

		StackState state;
		
		public FULL(StackState state) {
			if(DumpLevel.current()==DumpLevel.STACK)
				System.err.println("New state " + this.toString());
			this.state = state;
		}
		
		@Override
		public void register(ConstantsPool pool) {
			if(DumpLevel.current()==DumpLevel.STACK)
				System.err.println("Registering state " + this.toString());
			state.register(pool);
		}
		
		@Override
		public int length() {
			/*
			 full_frame {
			    u1 frame_type = FULL_FRAME; // 255 
			    u2 offset_delta;
			    u2 number_of_locals;
			    verification_type_info locals[number_of_locals];
			    u2 number_of_stack_items;
			    verification_type_info stack[number_of_stack_items];
			}
			*/
			if(DumpLevel.current()==DumpLevel.STACK)
				System.err.println("Computing length of state " + this.toString());
			return 1 + 2 + 2 + state.localsLength() + 2 + state.stackLength();
		}
		
		@Override
		public void writeTo(ByteWriter writer) {
			/*
			 full_frame {
			    u1 frame_type = FULL_FRAME; // 255 
			    u2 offset_delta;
			    u2 number_of_locals;
			    verification_type_info locals[number_of_locals];
			    u2 number_of_stack_items;
			    verification_type_info stack[number_of_stack_items];
			}
			*/
			if(DumpLevel.current()==DumpLevel.STACK)
				System.err.println("Writing state " + this.toString());
			if(deltaOffset==-1)
				throw new UnsupportedOperationException();
			writer.writeU1(255);
			writer.writeU2(deltaOffset);
			writer.writeU2(state.getLocals().size());
			state.getLocals().forEach((l)->
				l.writeTo(writer));
			writer.writeU2(state.getEntries().size());
			state.getEntries().forEach((e)->
				e.writeTo(writer));
		}

	}


}
