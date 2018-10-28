package prompto.debug;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class DebugRequestServer {

	LocalDebugger debugger;
	Thread thread;
	int port;
	boolean loop;
	
	public DebugRequestServer(LocalDebugger debugger) {
		this.debugger = debugger;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public int getPort() {
		return port;
	}

	public void startListening() throws Exception {
		Object lock = new Object();
		this.thread = new Thread(() -> {
			try(ServerSocket server = new ServerSocket(0)) {
				server.setSoTimeout(10); // make it fast to exit
				port = server.getLocalPort();
				LocalDebugger.showEvent("DebugRequestServer listening on " + port);
				synchronized(lock) {
					lock.notify();
				}			
				loop = true;
				LocalDebugger.showEvent("DebugRequestServer entering loop");
				while(loop) {
					try {
						Socket client = server.accept();
						handleMessage(client);
					} catch(SocketTimeoutException e) {
						// nothing to do, just helps exit the loop
					}
				}
				LocalDebugger.showEvent("DebugRequestServer exiting loop");
			} catch (Throwable t) {
				t.printStackTrace(System.err);
			}
		}, "Prompto debug server");
		this.thread.start();
		synchronized(lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// OK
			}
		}
	}
	
	

	public void stopListening() {
		loop = false;
		if(thread!=Thread.currentThread()) try {
			thread.join();
		} catch(InterruptedException e) {
		}
	}

	private void handleMessage(Socket client) throws Exception {
		synchronized(this) {
			// don't close these streams since that would close the underlying socket
			InputStream input = client.getInputStream();
			OutputStream output = client.getOutputStream();
			IDebugRequest request = readRequest(input);
			LocalDebugger.showEvent("DebugRequestServer receives " + request.getType());
			IDebugResponse response = request.execute(debugger);
			LocalDebugger.showEvent("DebugRequestServer responds " + response.getType());
			sendResponse(output, response);
			output.flush();
		}
	}

	private IDebugRequest readRequest(InputStream input) throws Exception {
		return Serializer.readDebugRequest(input);
	}


	private void sendResponse(OutputStream output, IDebugResponse response) throws Exception {
		Serializer.writeDebugResponse(output, response);
	}



}
