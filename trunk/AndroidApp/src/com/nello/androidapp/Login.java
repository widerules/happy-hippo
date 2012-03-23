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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private EditText username;
	private EditText passwd;
	private ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);    
        
        Button btForgotPwd = (Button) findViewById(R.id.btForgotPwd);
        btForgotPwd.setOnClickListener(this);
        btForgotPwd.getBackground().setColorFilter(new LightingColorFilter(0xFFFF0000, 0xBBFF0000)); 
        
        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btForgotPwd){
			// do something
			startActivity(new Intent(Login.this,RetrievePwd.class));
		}
		else if(v.getId() == R.id.btLogin){
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Login...");
			pd.setIndeterminate(true);
			pd.setCancelable(true);
			pd.show();
			userLogin();
		}
	}
	public void userLogin() {
	    // Create a new HttpClient and Post Header
		
		username = (EditText) findViewById(R.id.etUsername);
		passwd = (EditText) findViewById(R.id.etPasswd);
	
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.180digital.com/scores/login.php");
	        
	        try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("passwd", passwd.getText().toString()));
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
			        if(json.getInt("status")>0){
			        	pd.cancel();
			        	Toast.makeText(getApplicationContext(), "Logged Successfully", Toast.LENGTH_SHORT).show();
			        	SharedPreferences settings = getSharedPreferences("INFO", 0);
			        	SharedPreferences.Editor editor = settings.edit();
			        	editor.putInt("id", json.getInt("id"));
			        	editor.putString("username", username.getText().toString());
			        	editor.putString("passwd", passwd.getText().toString());
			        	editor.putString("email", json.getString("email"));
			        	editor.commit();
			        	startActivity(new Intent(Login.this,Games.class));
			        	
			        }
			        else {
			        	pd.cancel();
			        	Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
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
	        //return json;
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
