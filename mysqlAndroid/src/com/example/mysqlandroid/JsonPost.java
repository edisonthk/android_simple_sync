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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import android.util.Log;

public class JsonPost {
	
	public String line = "";
	public JsonPost(){
		// constructor
	}
	
	
	public JSONObject HttpRequest(String url, List<NameValuePair> params){
		InputStream input = null;
		JSONObject jObj = null;
		String json = "";
			
		
		try{
			StringBuilder builder = new StringBuilder();
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			//By implemented HttpPost class to definite your are using "POST" method
			
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			
						
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity = httpResponse.getEntity();				
				input = httpEntity.getContent();
				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(input));
				
				
				while((line = reader.readLine())!=null){
					builder.append(line);
				}
				input.close();
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
		} catch (Exception e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		
		return jObj;
	}
}


