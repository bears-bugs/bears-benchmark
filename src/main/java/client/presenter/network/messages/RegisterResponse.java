package client.presenter.network.messages;

public class RegisterResponse extends NetworkMessage {

	public final boolean success;
	public final boolean reason;//True=nickname taken, False=email taken
	
	/**
	 * Constructor for server
	 * @param success True if new player was registered successfully. False if not.
	 * @param reason False default. Set if player was not registered. T=nickname taken, F=email taken.
	 */
	public RegisterResponse(boolean success, boolean reason) {
		super(NET_MESSAGE_TYPE.REGISTER_RESPONSE);
		this.success = success;
		this.reason = reason;
		length = this.getDataString().getBytes().length;
	}

	/**
	 * Constructor for NetworkManager
	 * @param data String representation of the message
	 */
	public RegisterResponse(String data) {
		super(NET_MESSAGE_TYPE.REGISTER_RESPONSE);
		String[] splt = data.split(":");
		success = Boolean.parseBoolean(splt[1]);
		reason = Boolean.parseBoolean(splt[2]);
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+success+":"+reason;
	}

}
