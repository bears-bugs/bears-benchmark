package client.presenter.network.messages;

import java.util.Arrays;

public class InboxResponse extends NetworkMessage {

	public final int[] inviteIDs;
	public final String[] senders;
	public final String[] recipients;
	public final String[] sendDates;
	
	/**
	 * Constructor for Server
	 * @param inviteIDs List of invite IDS that the requester is a member of
	 * @param senders List of the senders nickname of each individual invite
	 * @param recipients List of the recipients nickname for each individual invite
	 * @param sendDates List of the send dates for each individual invite
	 */
	public InboxResponse(int[] inviteIDs, String[] senders, String[] recipients, String[] sendDates) {
		super(NET_MESSAGE_TYPE.INBOX_RESPONSE);
		this.inviteIDs = Arrays.copyOf(inviteIDs, inviteIDs.length);
		this.senders = Arrays.copyOf(senders, senders.length);
		this.recipients = Arrays.copyOf(recipients, recipients.length);
		this.sendDates = Arrays.copyOf(sendDates, sendDates.length);
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for RecieveThread
	 * @param data String representation of the message
	 */
	public InboxResponse(String data) {
		super(NET_MESSAGE_TYPE.INBOX_RESPONSE);
		String[] recs = data.split("#");
		this.inviteIDs = new int[recs.length];
		this.senders = new String[recs.length];
		this.recipients = new String[recs.length];
		this.sendDates = new String[recs.length];
		for(int i=0;i<recs.length;i++) {
			String[] splt = recs[i].split(":");
			if(i==0) {
				inviteIDs[i]=Integer.parseInt(splt[1]);
				senders[i]=splt[2];
				recipients[i]=splt[3];
				sendDates[i]=splt[4];
			}
			else {
				inviteIDs[i]=Integer.parseInt(splt[0]);
				senders[i]=splt[1];
				recipients[i]=splt[2];
				sendDates[i]=splt[3];
			}
		}
	}
	
	
	@Override
	public String getDataString() {
		String data = type.typeCode+":";
		for(int i=0;i<inviteIDs.length;i++) {
			if(i == inviteIDs.length-1)
				data+=inviteIDs[i]+":"+senders[i]+":"+recipients[i]+":"+sendDates[i];
			else
				data+=inviteIDs[i]+":"+senders[i]+":"+recipients[i]+":"+sendDates[i]+"#";
		}
		return data;
	}

}
