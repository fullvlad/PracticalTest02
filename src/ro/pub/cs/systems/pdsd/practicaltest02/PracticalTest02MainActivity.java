package ro.pub.cs.systems.pdsd.practicaltest02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PracticalTest02MainActivity extends Activity {
	
	// Server widgets
	//private EditText     serverPortEditText       = null;
	private Button       connectButton            = null;
	
	// Client widgets
	private EditText     clientUrlEditText    = null;
	private EditText     clientPortEditText       = null;
	
	private ServerThread serverThread             = null;
	private ClientThread clientThread             = null;
	
	private WebView webView = null;
	
	
	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			
			serverThread = new ServerThread(8080);
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			} else {
				Log.e("Test", "[MAIN ACTIVITY] Could not creat server thread!");
			}
			
		}
	}
	
	private GetButtonClickListener gettButtonClickListener = new GetButtonClickListener();
	private class GetButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String clientUrl = clientUrlEditText.getText().toString();
			String clientPort    = clientPortEditText.getText().toString();
			if (clientUrl == null || clientUrl.isEmpty() ||
				clientPort == null || clientPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Client connection parameters should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			if (serverThread == null || !serverThread.isAlive()) {
				Log.e("Test", "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
			
			String city = clientUrlEditText.getText().toString();
			if (city == null || city.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Parameters from client (city / information type) should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			webView.setText("");
			
			clientThread = new ClientThread(
					webView,
					Integer.parseInt(clientPort),
					clientUrl);
			clientThread.start();
		}
	}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.practical_test02_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
