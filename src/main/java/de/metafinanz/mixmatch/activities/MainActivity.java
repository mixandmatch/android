package de.metafinanz.mixmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.metafinanz.mixmatch.R;

public class MainActivity extends MixMatchActivity {
	
	private Intent intentAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentAppointments = new Intent(this, AppointmentsActivity.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void showImpressum(MenuItem item) {
    	Intent intent = new Intent(this, ImpressActivity.class);
    	startActivity(intent);
    }
    
    
    
    public void showLocations(View view) {
    	Intent intent = new Intent(this, LocationsActivity.class);
    	startActivity(intent);
    }
    
    public void showAppointments(View view) {
    	
    	String userName = getUsername();
    	if (userName != null && userName.length() > 0) {
    		startActivity(this.intentAppointments);
    	} else {
    		EditText text = (EditText) findViewById(R.id.textUsername);
    		text.setVisibility(EditText.VISIBLE);
    		Button buttonSave = (Button) findViewById(R.id.buttonSaveUsername);
    		buttonSave.setVisibility(View.VISIBLE);
    	}    	    	
    }
    
    public void saveUsername(View view) {
    	EditText userText = (EditText) findViewById(R.id.textUsername);
    	String username = userText.getText().toString();
    	setUsername(username);
    	startActivity(this.intentAppointments);
    }
    
}
