package client.presenter.network.messages;

public class ActiveGameRequest extends NetworkMessage {

	public final String nickname;
	
	/**
	 * Constructor for presenter
	 * @param nickname the nickname of the logged in player, from presenter/login prompt
	 */
	public ActiveGameRequest(String nickname) {
		super(NET_MESSAGE_TYPE.ACTIVE_GAMES_REQUEST);
		this.nickname = nickname;
		length = this.getDataString().getBytes().length;
	}

	/**
	 * Constructor for server
	 * Expected: "9:nickname"
	 * @param data String representation of the message
	 * @param off dummy variable to differentiate constructors
	 */
	public ActiveGameRequest(String data, int off) {
		super(NET_MESSAGE_TYPE.ACTIVE_GAMES_REQUEST);
		this.nickname = data.split(":")[1];
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+nickname;
	}

}
