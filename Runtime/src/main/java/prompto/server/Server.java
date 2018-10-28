package prompto.server;

import prompto.value.Document;

public abstract class Server {
	
	static ThreadLocal<String> httpUser = new ThreadLocal<>();
	
	public static String getHttpUser() {
		return httpUser.get();
	}
	
	public static void setHttpUser(String user) {
		httpUser.set(user);
	}
	
	static ThreadLocal<Document> httpSession = new ThreadLocal<>();

	public static Document getHttpSession() {
		return httpSession.get();
	}
	
	public static void setHttpSession(Document session) {
		httpSession.set(session);
	}
	

}
