package client.presenter.network.messages;

import java.util.Arrays;

public class ActiveGameResponse extends NetworkMessage {

	public final int[] gameIDs;
	public final String[] gameBoards;
	public final String[] opponents;
	public final String[] startDates;
	
	/**
	 * Constructor for the server
	 * @param gameIDs Arrays of game IDs
	 * @param gameBoards Array of game boards in string form
	 * @param opponents Array of opponent nicknames
	 * @param startDates Array of start dates of the games
	 */
	public ActiveGameResponse(int[] gameIDs, String[] gameBoards, String[] opponents, String[] startDates) {
		super(NET_MESSAGE_TYPE.ACTIVE_GAMES_RESPONSE);
		this.gameIDs = Arrays.copyOf(gameIDs, gameIDs.length);
		this.gameBoards = Arrays.copyOf(gameBoards, gameBoards.length);
		this.opponents = Arrays.copyOf(opponents, opponents.length);
		this.startDates = Arrays.copyOf(startDates, startDates.length);
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for RecieveThread
	 * @param data String representation of the message
	 */
	public ActiveGameResponse(String data) {
		super(NET_MESSAGE_TYPE.ACTIVE_GAMES_RESPONSE);
		String[] recs = data.split("#");
		this.gameIDs = new int[recs.length];
		this.gameBoards = new String[recs.length];
		this.opponents = new String[recs.length];
		this.startDates = new String[recs.length];
		for(int i=0;i<recs.length;i++) {
			String[] splt = recs[i].split(":");
			if(i==0) {
				gameIDs[i]=Integer.parseInt(splt[1]);
				gameBoards[i]=splt[2];
				opponents[i]=splt[3];
				startDates[i]=splt[4];
			}
			else {
				gameIDs[i]=Integer.parseInt(splt[0]);
				gameBoards[i]=splt[1];
				opponents[i]=splt[2];
				startDates[i]=splt[3];
			}
		}
	}

	@Override
	public String getDataString() {
		String data = type.typeCode+":";
		for(int i=0;i<gameIDs.length;i++) {
			if(i == gameIDs.length-1)
				data+=gameIDs[i]+":"+gameBoards[i]+":"+opponents[i]+":"+startDates[i];
			else
				data+=gameIDs[i]+":"+gameBoards[i]+":"+opponents[i]+":"+startDates[i]+"#";
		}
		return data;
	}

}
