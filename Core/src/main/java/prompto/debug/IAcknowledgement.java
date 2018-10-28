package prompto.debug;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface IAcknowledgement {

	@JsonIgnore
	Type getType();

	static class Acknowledgement implements IAcknowledgement {

		@Override
		public Type getType() {
			return Type.RECEIVED;
		}
		
	}
	
	public enum Type {
		RECEIVED(Acknowledgement.class);

		Class<? extends IAcknowledgement> klass;
		
		Type(Class<? extends IAcknowledgement> klass) {
			this.klass = klass;
		}
		
		public Class<? extends IAcknowledgement> getKlass() {
			return klass;
		}
	}


}
