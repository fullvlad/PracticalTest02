package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.client.ClientProtocolException;

//import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
import android.util.Log;

public class ServerThread extends Thread {
	
	private int          port         = 0;
	private ServerSocket serverSocket = null;
	
	
	public ServerThread(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			Log.e("Test", "An exception has occurred: " + ioException.getMessage());
			ioException.printStackTrace();
			
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setServerSocker(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	
	@Override
	public void run() {
		try {		
			while (!Thread.currentThread().isInterrupted()) {
				Log.i("Test", "[SERVER] Waiting for a connection...");
				Socket socket = serverSocket.accept();
				Log.i("Test", "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
				CommunicationThread communicationThread = new CommunicationThread(socket);
				communicationThread.start();
			}			
		} catch (ClientProtocolException clientProtocolException) {
			Log.e("Test", "An exception has occurred: " + clientProtocolException.getMessage());
			clientProtocolException.printStackTrace();	
		} catch (IOException ioException) {
			Log.e("Test", "An exception has occurred: " + ioException.getMessage());
			ioException.printStackTrace();
		}
	}
	
	public void stopThread() {
		if (serverSocket != null) {
			interrupt();
			try {
				serverSocket.close();
			} catch (IOException ioException) {
				Log.e("Test", "An exception has occurred: " + ioException.getMessage());
					ioException.printStackTrace();
			}
		}
	}

}