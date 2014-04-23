package com.example.mysqlandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle; 
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainThread extends Activity {

	private JsonParser jParser = new JsonParser();
	private JsonPost jPost = new JsonPost();
		
	private static String url_updateData = "http://10.0.2.2/updateData.php";
	private static String url_readData = "http://10.0.2.2/readData.php";
	
	/* You have to replace 0.0.0.0 to your server IP address.
	 * 
	 * If you are using XAMPP, you need to put your own PC ip address 
	 * that you are using to browsing internet. You can check it by using
	 * shell command -ipconfig for windows or shell command -ifconfig for linux
	 * 
	 * If you are using android emulator use Ip address above 10.0.2.2
	 * For more information, check it out with http://developer.android.com/tools/devices/emulator.html 
	 */	
	
	private EditText edittext;
	private TextView serverText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_layout);
		edittext = (EditText)findViewById(R.id.text);
		serverText = (TextView)findViewById(R.id.serverText);
		
		readDatabase readDb = new readDatabase();
		readDb.execute();
		
		/*
		 * To allow sending the message to server when enter is pressed. 
		 * OnEditorActionListener class need to be implemented
		 */
		
		TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN){
					// ACTION_DOWN == 'enter'
					//android:imeOptions = "actionUnspecified"
					
					/* Caution !!!
					 * Some android swype pad enter button doesn't work.
					 * Because the enter icon button is stands of 'done' or dismiss keyboard
					 * not 'enter'
					 * It is recommend using T9 keyboard instead of swype keyboard.   
					 */
					
					new updateDatabase().execute();
				}
				return false;
			}
		};
		
		edittext.setOnEditorActionListener(editorListener);
				
		/* Make the application keep update to mysql server
		 * this will make it look like sync with desktop but in fact
		 * android retrieve data from mysql server just like what web server do
		 * 
		 * In order to make android retrieve data automatically
		 * I use timertask. Making android keep reading data for every 8000milli seconds   
		 *  
		*/
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask desktopTextUpdate = new TimerTask(){
			@Override
			public void run(){
				handler.post(new Runnable(){
					public void run(){
						try{							
							readDatabase readDb = new readDatabase();
							readDb.execute();						
							
						}catch(Exception e){
							Log.e("Error", "something wrong with handler");
						}
					}
				});
			}
		};
		timer.schedule(desktopTextUpdate, 15000, 2000); 
		//timertask will start at 15000ms and carry out every 8000ms
		
	}
	 

	
	/* 
	 *If your application is Android 2.0 above, AsyncTask class is required.
	 *
	 * AsyncTask class allow some works run in background of android. 
	 * Some works are performing a potentially expensive operation on UI thread might make
	 * your application crash and it will throw NetworkOnMainThreadException.
	 * It is why we need AsyncTask class to perform a heavy operation task on 
	 * background of the thread.
	 *  
	 */
	
	//readDatabase is using "GET" method to retrieve data
	//so I will implement JsonParser to following readDatabase class
	
	private class readDatabase extends AsyncTask<String,Void,String>{ 
		
		private String updateContent = "";
		
		@Override
		protected void onPreExecute(){
			}
		
		protected String doInBackground(String... url){
			
			try{
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();				
				JSONObject json = jParser.HttpRequest(url_readData, params);				
				updateContent = json.getString("desktopTalk");
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return "";
		}
		
		protected void onPostExecute(String result)
		{
			
			serverText.setText(updateContent);
		}
		
		@Override
		protected void onProgressUpdate(Void... values){			
		}
		
	}
	
	// updateDatabase must using "POST" method to send data
	// so I will implement JsonPost to following updateDatabase class
	
	private class updateDatabase extends AsyncTask<String,Void,String>{ 
		private ProgressDialog pDialog;
		private String content = "";		
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
	        pDialog = new ProgressDialog(MainThread.this);
            pDialog.setMessage("Synchronizing. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

		}
		
		protected String doInBackground(String... url){
			
			try{				
				content = edittext.getText().toString();
				
				if(content != null){
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					
					/* BasicNameValuePair
					 * 1st parameter -> $_POST['column']
					 * 2nd parameter -> text input to $_POST['column']
					 */
					params.add(new BasicNameValuePair("column","androidTalk"));					
					params.add(new BasicNameValuePair("text",content));
					jPost.HttpRequest(url_updateData, params);		
				}				
									
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return "";
		}
		
		protected void onPostExecute(String result){
			pDialog.dismiss();
			edittext.setText("");
		}
		
		@Override
		protected void onProgressUpdate(Void... values){
			
		}
		
	}
	
}
