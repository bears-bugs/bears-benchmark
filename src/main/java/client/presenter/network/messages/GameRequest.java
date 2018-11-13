package client.presenter.network.messages;

public class GameRequest extends NetworkMessage {

	public final int gameID;
	
	/**
	 * Constructor for presenter
	 * @param gameID ID of game requesting to play, retrieved from active game view
	 */
	public GameRequest(int gameID) {
		super(NET_MESSAGE_TYPE.GAME_REQUEST);
		this.gameID = gameID;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of message received
	 */
	public GameRequest(String data) {
		super(NET_MESSAGE_TYPE.GAME_REQUEST);
		this.gameID = Integer.parseInt(data.split(":")[1]);
		length = getDataString().getBytes().length;
	}

	@Override
	public String getDataString() {
		return type.typeCode+":"+gameID;
	}

}
