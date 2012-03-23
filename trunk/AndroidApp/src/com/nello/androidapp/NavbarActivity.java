package com.nello.androidapp;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class NavbarActivity  extends Activity {
 
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.actnavbar);
 
    RadioButton radioButton;
    radioButton = (RadioButton) findViewById(R.id.btnAll);
    radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
    radioButton = (RadioButton) findViewById(R.id.btnPicture);
    radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
    radioButton = (RadioButton) findViewById(R.id.btnVideo);
    radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
    radioButton = (RadioButton) findViewById(R.id.btnFile);
    radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
  }
 
  public CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (isChecked) {
        Toast.makeText(NavbarActivity.this, buttonView.getText(), Toast.LENGTH_SHORT).show();
      }
    }
  };
}