package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.webkit.WebView;

public class ClientThread extends Thread {
	
	private String   address;
	private int      port;
	private String   url;
	private WebView webView;
	
	private Socket   socket;
	
	public ClientThread(			
			WebView view,
			int port,
			String url) {
		this.port                    = port;
		this.url                     = url;
		this.webView = view;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e("Test", "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(url);
				printWriter.flush();
				String result;
				while ((result = bufferedReader.readLine()) != null) {
					final String finalizedBodyResult = result;
					webView.post(new Runnable() {
						@Override
						public void run() {
							webView.loadData(finalizedBodyResult, "text/html", null);
						}
					});
				}
			} else {
				Log.e("Test", "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e("Test", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
				ioException.printStackTrace();
		}
	}

}