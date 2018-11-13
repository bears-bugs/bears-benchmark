package client.presenter.network.messages;

public class Login extends NetworkMessage{
	public final String email;
	public final String passwordAttempt;
	
	/**
	 * Constructor for Presenter
	 * @param userEmail email keyed in
	 * @param passwordHash Hash of keyed in password attempt
	 */
	public Login(String userEmail, String passwordHash) {
		super(NET_MESSAGE_TYPE.LOGIN);
		email = userEmail;
		passwordAttempt = passwordHash;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for Server
	 * Expected: 1:username:passwordHash
	 * @param data String representation of the message
	 */
	public Login(String data) {
		super(NET_MESSAGE_TYPE.LOGIN);
		String[] spilt = data.split(":");
		email = spilt[1];
		passwordAttempt = spilt[2];
		length = getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return new String(type.typeCode+":"+email+":"+passwordAttempt);
	}
	
}
