package client.presenter.network.messages;

public class InviteResponse extends NetworkMessage {

	public final int inviteID;
	public final boolean status;//T=accepted, F=declined or cancelled
	
	/**
	 * Constructor for response
	 * @param inviteID Invite ID, from inbox view
	 * @param status True if the invite is accepted, false if the invite is declined or cancelled
	 */
	public InviteResponse(int inviteID, boolean status) {
		super(NET_MESSAGE_TYPE.INVITE_RESPONSE);
		this.inviteID = inviteID;
		this.status = status;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of the message
	 */
	public InviteResponse(String data) {
		super(NET_MESSAGE_TYPE.INVITE_RESPONSE);
		String[] splt = data.split(":");
		this.inviteID = Integer.parseInt(splt[1]);
		this.status = Boolean.parseBoolean(splt[2]);
		length = this.getDataString().getBytes().length;
	}

	@Override
	public String getDataString() {
		return type.typeCode+":"+inviteID+":"+status;
	}

}
