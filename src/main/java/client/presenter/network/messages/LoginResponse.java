package client.presenter.network.messages;

public class LoginResponse extends NetworkMessage {
	public final boolean success;
	public final String nickname;
	
	/**
	 * Constructor for server
	 * @param status True for login success, false for failure
	 * @param nickname the nickname of the request login if success, null if failure
	 */
	public LoginResponse(boolean status, String nickname) {
		super(NET_MESSAGE_TYPE.LOGIN_RESPONSE);
		success = status;
		this.nickname = nickname;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for RecieveThread
	 * Expected 2:(true/false):nickname
	 * @param data Data String read from socket
	 */
	public LoginResponse(String data) {
		super(NET_MESSAGE_TYPE.LOGIN_RESPONSE);
		String[] splt = data.split(":");
		success = Boolean.parseBoolean(splt[1]);
		nickname = splt[2];
		length = getDataString().getBytes().length;
	}

	@Override
	public String getDataString() {
		return type.typeCode+":"+success+":"+nickname;
	}
}
