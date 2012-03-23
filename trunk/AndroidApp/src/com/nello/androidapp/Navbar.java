package com.nello.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Navbar extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navbar);
	}
	public void initializeBottomBar(){
		Button btGames = (Button) findViewById(R.id.btGames);
		btGames.setOnClickListener(this);
		
		Button btFriends = (Button) findViewById(R.id.btFriends);
		btFriends.setOnClickListener(this);
		
		Button btScores = (Button) findViewById(R.id.btScores);
		btScores.setOnClickListener(this);
		
		Button btHistory = (Button) findViewById(R.id.btHistory);
		btHistory.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btGames){
			startActivity(new Intent(this,Games.class));
			//Toast.makeText(getApplicationContext(), "Games", Toast.LENGTH_SHORT).show();
		}
		else if(v.getId() == R.id.btFriends){
			startActivity(new Intent(this,Friends.class));
			//Toast.makeText(getApplicationContext(), "Games", Toast.LENGTH_SHORT).show();

		}
		else if(v.getId() == R.id.btScores){
			
		}
		else if(v.getId() == R.id.btHistory){
			
		} else{
			Toast.makeText(getApplicationContext(), "Notte", Toast.LENGTH_SHORT).show();
		}
	}
}
