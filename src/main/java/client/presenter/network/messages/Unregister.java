package client.presenter.network.messages;

public class Unregister extends NetworkMessage {
	
	public final String email;
	public final String nickname;
	public final String password;
	
	/**
	 * Constructor for presenter
	 * A user must know all of the information about an account to unregister
	 * @param email User email keyed in
	 * @param nickname User email keyed in
	 * @param password Hash of password keyed in
	 */
	public Unregister(String email, String nickname, String password) {
		super(NET_MESSAGE_TYPE.UNREGISTER);
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		length = this.getDataString().getBytes().length;
	}

	/**
	 * Constructor for server
	 * Expected: "5:email:nickname:password"
	 * @param data string read in from RecieveThread
	 */
	public Unregister(String data) {
		super(NET_MESSAGE_TYPE.UNREGISTER);
		String[] splt = data.split(":");
		email = splt[1];
		nickname = splt[2];
		password = splt[3];
		length = getDataString().getBytes().length;
	}
	
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+email+":"+nickname+":"+password;
	}

}
