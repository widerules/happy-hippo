package com.nello.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

public class Friends extends Navbar {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		SharedPreferences settings = getSharedPreferences("INFO", 0);
		int id = settings.getInt("id", 0);
		if(id == 0){
			startActivity(new Intent(Friends.this,Main.class));
		} else {
			initializeBottomBar();
		}
		//String id = ""+settings.getString("id", "");
		//Log.d("NELLO",""+settings.getInt("id", 0));
		//Toast.makeText(getApplicationContext(), ""+settings.getInt("id", 0), Toast.LENGTH_SHORT).show();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mymenus, menu);
    	
    	return true;
    }

}
