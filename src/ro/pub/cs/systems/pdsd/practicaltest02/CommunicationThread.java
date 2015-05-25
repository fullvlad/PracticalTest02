package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class CommunicationThread extends Thread {
	
	private Socket       socket;
	
	public CommunicationThread(Socket socket) {
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				String bodyData = "";
				if (bufferedReader != null && printWriter != null) {
					Log.i("Test", "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String url            = bufferedReader.readLine();
					Log.i("Test", "[COMMUNICATION THREAD] Getting the information from the webservice...");
					HttpClient httpClient = new DefaultHttpClient();
					if (!url.toLowerCase().contains("bad")) {
						
						HttpGet httpGet = new HttpGet(url);
						
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String pageSourceCode = httpClient.execute(httpGet, responseHandler);
						if (pageSourceCode != null) {
							Document document = Jsoup.parse(pageSourceCode);
							Element element = document.child(0);
							Elements bodies = element.getElementsByTag("body");
							
							Element body = bodies.get(0);
								
							bodyData = body.data();

							
						} else {
							Log.e("Test", "[COMMUNICATION THREAD] Error getting the information from the webservice!");
						}
					} else {
						Log.e("Test", "[COMMUNICATION THREAD] URL contains BAD!!!");
					}

					printWriter.println(bodyData);
					printWriter.flush();
						
				} else {
					Log.e("Test", "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				Log.e("Test", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				ioException.printStackTrace();
			} 
		} else {
			Log.e("Test", "[COMMUNICATION THREAD] Socket is null!");
		}
	}

}