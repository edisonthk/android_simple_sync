package com.example.mysqlandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import android.util.Log;

public class JsonParser {
	
	public String line = "";
	public JsonParser(){
		// constructor
	}
	
	
	public JSONObject HttpRequest(String url, List<NameValuePair> params){
		InputStream input = null;
		JSONObject jObj = null;
		String json = "";
		
		
		StringBuilder builder = new StringBuilder();
		DefaultHttpClient client = new DefaultHttpClient();
		URLEncodedUtils.format(params,"utf-8"); //
		
		//By implement HttpGet class to definite you are using "GET" method
		HttpGet httpGet = new HttpGet(url);
				
		try{			
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				input = entity.getContent();
				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(input));
				
				
				while((line = reader.readLine())!=null){
					builder.append(line);
				}
				json = builder.toString();
				
			} else {
				Log.e("JSON Parser","Failed to download file");
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try {
			jObj = new JSONObject(json);
			input.close();
		} catch (Exception e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		
		return jObj;
	}
}


