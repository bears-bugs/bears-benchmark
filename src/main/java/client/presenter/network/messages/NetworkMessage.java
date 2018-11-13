package client.presenter.network.messages;

/**
 * Abstract class for net messages' shared functionality
 * @author bcgdwn
 *
 */
public abstract class NetworkMessage {
	
	/**
	 * Message type
	 */
	public final NET_MESSAGE_TYPE type;
	/**
	 * Length of data String
	 */
	public int length; 
	/**
	 * 
	 * @return String form of message
	 */
	public abstract String getDataString();
	
	/**
	 * 
	 * @param Type sets the type field
	 */
	public NetworkMessage(NET_MESSAGE_TYPE Type) {
		type = Type;
	}
	
}
