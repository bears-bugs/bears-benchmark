package client.presenter.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import client.presenter.network.messages.*;

public class Sender {
	private Socket sock;
	private DataOutputStream outToServer;
	
	
	public Sender(Socket Sock) throws IOException {
		sock = Sock;
		outToServer = new DataOutputStream(sock.getOutputStream());
	}
	
	
	public boolean sendToServer(NetworkMessage msg) {
		try {
			outToServer.writeInt(msg.length);
			outToServer.write(msg.getDataString().getBytes());
			outToServer.flush();
			return true;
		} catch (IOException e) {
			System.err.println("Failed to send message: "+msg.getDataString());
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	
	
}
