package client.presenter.network.messages;

public class InboxRequest extends NetworkMessage {

	public final int userID;
	
	/**
	 * Constructor for presenter
	 * @param userID The logged in user's ID
	 */
	public InboxRequest(int userID) {
		super(NET_MESSAGE_TYPE.INBOX_REQUEST);
		this.userID = userID;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of the message 
	 */
	public InboxRequest(String data) {
		super(NET_MESSAGE_TYPE.INBOX_REQUEST);
		this.userID = Integer.parseInt(data.split(":")[1]);
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+userID;
	}

}
