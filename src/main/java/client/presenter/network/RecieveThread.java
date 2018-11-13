package client.presenter.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import client.presenter.network.messages.*;

public class RecieveThread extends Thread{
	private Socket sock;
	private DataInputStream din;
	private NetworkManager mgmt;
	
	
	/**
	 * Constructor, instantiates DataInputStream of socket
	 * @param Sock socket connected to server from NetworkManager
	 * @param net NetworkManager, to send message up the chain
	 * @throws IOException thrown if getting DataInputStream fails
	 */
	public RecieveThread(Socket Sock, NetworkManager net) throws IOException {
		sock = Sock;
		din = new DataInputStream(sock.getInputStream());
		mgmt = net;
	}
	
	
	/**
	 * Run loop, waits to be active to read in data
	 */
	@Override
	public void run() {
		int dataLen;
		while(!Thread.currentThread().isInterrupted()) {
			try {
				dataLen = din.readInt();
				byte[] bytes = new byte[dataLen];
				din.readFully(bytes, 0, dataLen);
				String msg = new String(bytes);
				parseMessage(msg);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Reads message type code, sends to appropriate NetworkManager method to handle message type
	 * @param msg String of message received 
	 */
	protected void parseMessage(String msg) {
		NET_MESSAGE_TYPE mt = NET_MESSAGE_TYPE.fromInt(Integer.parseInt(msg.split(":")[0]));
		NetworkMessage message = null;
		switch(mt) {
			case LOGIN: message = new Login(msg);
			case LOGIN_RESPONSE: message = new LoginResponse(msg);
			case LOGOUT: message = new Logout(msg);
			case REGISTER: message = new Register(msg);
			case UNREGISTER: message = new Unregister(msg);
			case GAME_REQUEST: message = new GameRequest(msg);
			case GAME_INFO: message = new GameInfo(msg);
			case MOVE: message = new Move(msg);
			case ACTIVE_GAMES_REQUEST: message = new ActiveGameRequest(msg);
			case ACTIVE_GAMES_RESPONSE: message = new ActiveGameResponse(msg);
			case INVITE_REQUEST: message = new InviteRequest(msg);
			case INVITE_RESPONSE: message = new InviteResponse(msg);
			case RESIGN: message = new Resign(msg);
			case REGISTER_RESPONSE: new RegisterResponse(msg);
			case INBOX_REQUEST: message = new InboxRequest(msg);
			case INBOX_RESPONSE:
		}
		if(message!=null)
			mgmt.sendToPresenter(message);
	}
	
	
}
