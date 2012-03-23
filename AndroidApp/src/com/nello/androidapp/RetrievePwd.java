package com.nello.androidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

public class RetrievePwd extends Activity implements OnClickListener{
	
	private ProgressDialog pd;
	private EditText etUsername;
	private EditText etEmail;	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrievepwd);  
        
        Button btNewProfile = (Button) findViewById(R.id.btNewProfile);
        btNewProfile.setOnClickListener(this);
        btNewProfile.getBackground().setColorFilter(new LightingColorFilter(0xFFFF0000, 0xBBFF0000)); 
        
        Button btRetrievePwd = (Button) findViewById(R.id.btRetrievePwd);
        btRetrievePwd.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btNewProfile){
			startActivity(new Intent(RetrievePwd.this,Main.class));
		}
		else if(v.getId()==R.id.btRetrievePwd){
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Connecting...");
			pd.setIndeterminate(true);
			pd.setCancelable(true);
			pd.show();
			resetPasswd();
		}
	}
	
	public void resetPasswd(){
		etUsername = (EditText) findViewById(R.id.etUsername);
		etEmail = (EditText) findViewById(R.id.etEmail);
		String username = etUsername.getText().toString().trim();
		String email = etEmail.getText().toString().trim();
		if(email.equals("") && username.equals("")){
			pd.cancel();
			return;
		} else {
			
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://www.180digital.com/scores/recover_pwd.php");
		        
		    try {
			        // Add your data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("username", username));
			        nameValuePairs.add(new BasicNameValuePair("email", email));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			        // Execute HTTP Post Request
			        HttpResponse response;
			        
			        JSONObject json = new JSONObject();
		        	response = httpclient.execute(httppost);
		         
		        	HttpEntity entity = response.getEntity();
		         
		        	if (entity != null) {
		         
				        // A Simple JSON Response Read
				        InputStream instream = entity.getContent();
				        String result = convertStreamToString(instream);

				        json = new JSONObject(result);
			        	pd.cancel();	
				        if(json.getInt("status") > 0){
				        	Toast.makeText(getApplicationContext(), "Password reset.\nPlease, check your email.", Toast.LENGTH_SHORT).show();
				        	SharedPreferences settings = getSharedPreferences("INFO", 0);
				        	SharedPreferences.Editor editor = settings.edit();
				        	editor.putLong("id", json.getInt("id"));
				        	editor.putString("username", username);
				        	editor.putString("email", email);
				        	editor.commit();		        	
				        }
				        else {
				        	Toast.makeText(getApplicationContext(), "Your password couldn't be reset.", Toast.LENGTH_SHORT).show();
					    }
				        instream.close();
		        	}
		         
		    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
		    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
		    } catch (JSONException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
		    }	         
		 }
	}
	/**
	*
	* @param is
	* @return String
	*/
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		 
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
