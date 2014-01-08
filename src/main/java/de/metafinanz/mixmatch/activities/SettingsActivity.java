package de.metafinanz.mixmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import de.metafinanz.mixmatch.R;

public class SettingsActivity extends MixMatchActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	public void saveUsername(View view) {
		EditText userText = (EditText) findViewById(R.id.textSettingsUsername);
    	String username = userText.getText().toString();
    	setUsername(username);
    	finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
