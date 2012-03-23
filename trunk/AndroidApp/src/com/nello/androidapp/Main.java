package com.nello.androidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	private String myString;
	private EditText etUsername;
	private EditText etEmail;
	private EditText etPasswd;
	private EditText etPasswd2;
	private ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        userLogin();
        
        setContentView(R.layout.main);
        
        Button btAlreadyUser = (Button) findViewById(R.id.btAlreadyUser);
        btAlreadyUser.setOnClickListener(this);
        btAlreadyUser.getBackground().setColorFilter(new LightingColorFilter(0xFFFF0000, 0xBBFF0000)); 
        
        Button btNewProfile = (Button) findViewById(R.id.btNewProfile);
        btNewProfile.setOnClickListener(this);
        /*
        SharedPreferences settings = getSharedPreferences("STRING", 0);
        myString = settings.getString("id", "");
        
        SQLiteDatabase db = openOrCreateDatabase("myDb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS profiles (username VARCHAR, email VARCHAR, passwd VARCHAR, id INT);");
        db.execSQL("INSERT INTO profiles VALUES ('afavaro','antonellofavaro@yahoo.com','lorini',1);");
        Cursor c = db.rawQuery("SELECT * FROM profiles", null);
        c.moveToFirst();
        Log.d("NELLO",c.getString(c.getColumnIndex("email")));
        db.close();
        */
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mymenus, menu);
    	
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	if(item.getItemId()==R.id.item1){
    		
    	}
    	else if(item.getItemId()==R.id.item2){
    		
    	}
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btAlreadyUser){
			startActivity(new Intent(Main.this,Login.class));
		}
		else if(v.getId()==R.id.btNewProfile){
			//attempt to create new profile
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Connecting...");
			pd.setIndeterminate(true);
			pd.setCancelable(true);
			pd.show();
			createNewProfile();
		}
	}

	public void createNewProfile() {
	    // Create a new HttpClient and Post Header
		etUsername = (EditText) findViewById(R.id.etUsername);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPasswd = (EditText) findViewById(R.id.etPasswd);
		etPasswd2 = (EditText) findViewById(R.id.etPasswd2);
		
		String username = (String) etUsername.getText().toString().trim();
		String passwd = (String) etPasswd.getText().toString().trim();
		String passwd2 = (String) etPasswd2.getText().toString().trim();
		String email =  (String) etEmail.getText().toString().trim();
		
		String msg = "";
		if(username.equals("")){
			msg = "- Username is missing;\n";
		} else if (username.length() < 6 || username.length() > 10){
			msg = "- Username must be between 6 and 10 characters long;\n";
		}
		if(email.equals("")){
			msg += "- Email is missing;\n";
		}
		if(passwd.equals("")){
			msg += "- Password is missing;\n";
		} else if (passwd.length() < 6 || passwd.length() > 10){
			msg = "- Password must be between 6 and 10 characters long;\n";			
		}
		if(!passwd.equals(passwd2)){
			msg += "- The Retyped Password doesn't match;\n";
		}
		if(!msg.equals("")){
			pd.cancel();
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			return;
		}
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.180digital.com/scores/create_profile.php");
	        
	        try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", username));
		        nameValuePairs.add(new BasicNameValuePair("passwd", passwd));
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
			        	Toast.makeText(getApplicationContext(), "Profile Created", Toast.LENGTH_SHORT).show();
			        	SharedPreferences settings = getSharedPreferences("INFO", 0);
			        	SharedPreferences.Editor editor = settings.edit();
			        	editor.putInt("id", json.getInt("id"));
			        	editor.putString("username", username);
			        	editor.putString("passwd", passwd);
			        	editor.putString("email", email);
			        	editor.commit();
			        	startActivity(new Intent(Main.this,Games.class));
			        }
			        else if (json.getInt("status") == -1){
			        	Toast.makeText(getApplicationContext(), "Username or Email alredy exists", Toast.LENGTH_SHORT).show();
			        } else {
			        	Toast.makeText(getApplicationContext(), "Your profile couldn't be created", Toast.LENGTH_SHORT).show();
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
	public void userLogin() {
	    // Create a new HttpClient and Post Header
		SharedPreferences settings = getSharedPreferences("INFO", 0);
		String username = settings.getString("username", "");
		String passwd = settings.getString("passwd", "");
	
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.180digital.com/scores/login.php");
	        
	        try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", username));
		        nameValuePairs.add(new BasicNameValuePair("passwd", passwd));
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
			        	//pd.cancel();
			        	Toast.makeText(getApplicationContext(), "Logged Successfully", Toast.LENGTH_SHORT).show();
			        	startActivity(new Intent(Main.this,Games.class));			        	
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