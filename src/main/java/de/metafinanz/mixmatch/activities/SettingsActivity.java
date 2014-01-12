package de.metafinanz.mixmatch.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import de.metafinanz.mixmatch.R;

/**
 * Activity, zum Speichern des Nutzernamens.
 * @author ulf
 *
 */
public class SettingsActivity extends MixMatchActivity {

	private EditText userText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);		
		// Show the Up button in the action bar.
		userText = (EditText) findViewById(R.id.textSettingsUsername);
		
		userText.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				userText.setText("");
				userText.setTextColor(Color.BLACK);
			}
		});
		
		String username = getUsername();
		
		if (username != null && username.length() > 0) {
			userText.setText(getUsername());
		} 				
	}
	
	
	public void saveUsername(View view) {
		EditText userText = (EditText) findViewById(R.id.textSettingsUsername);
    	String username = userText.getText().toString();
    	setUsername(username);
    	userText.setTextColor(Color.DKGRAY);
    	finish();
	}

}
