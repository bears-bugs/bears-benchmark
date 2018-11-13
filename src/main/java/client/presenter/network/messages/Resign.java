package client.presenter.network.messages;

public class Resign extends NetworkMessage {

	public final int gameID;
	public final String nickname;
	
	/**
	 * Constructor for presenter.
	 * @param gameID Game ID, from the active game view
	 * @param nickname Nickname of player resigning, logged in player.
	 */
	public Resign(int gameID, String nickname) {
		super(NET_MESSAGE_TYPE.RESIGN);
		this.gameID = gameID;
		this.nickname = nickname;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server.
	 * Expected: "13:gameID:nickname"
	 * @param data String representation of the message
	 */
	public Resign(String data) {
		super(NET_MESSAGE_TYPE.RESIGN);
		String[] splt = data.split(":");
		this.gameID = Integer.parseInt(splt[1]);
		this.nickname = splt[2];
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+gameID+":"+nickname;
	}

}
