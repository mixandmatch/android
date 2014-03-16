package de.metafinanz.mixmatch.activities;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.activities.dialogs.QuitApplicationAlertDialogFragment;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class MainActivity extends MixMatchActivity {
	
	private QuitApplicationAlertDialogFragment quitDialog = new QuitApplicationAlertDialogFragment();
	private Intent intentAppointments;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentAppointments = new Intent(this, AppointmentsActivity.class);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(false);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	// Asychroner Task für REST-Service
		AsyncTask<Void, Void, List<Location>> asyncTask = new AsyncTask<Void, Void, List<Location>>() {

			@Override
			protected List<Location> doInBackground(Void... params) {
				List<Location> list = service.getLocations();
				return list;
			}

			@Override
			protected void onPostExecute(List<Location> result) {
				super.onPostExecute(result);
				Log.i("MainActivity", result.toString());
				for (Location location : result) {
					Log.i("MainActivity", location.getLocationName());
				}
			}
		};
		asyncTask.execute();
    }
    
    @Override
    public void finish() {
    	super.finish();
    	Log.i("MainActivity", "Activity Main wurde beendet");
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
    	overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    
    public void showAppointments(View view) {
    	
    	String userName = getUsername();
    	if (userName != null && userName.length() > 0) {
    		startActivity(this.intentAppointments);
    		overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
    	
    	AsyncTask<String, Void, User> asyncTask = new AsyncTask<String, Void, User>() {
    		@Override
    		protected User doInBackground(String... params) {
    			return service.getOrCreateUser(params[0]);
    		}
    		
    		@Override
    		protected void onPostExecute(User result) {
    			super.onPostExecute(result);
    		}
    	};
    	
    	asyncTask.execute(username);
    	
    	startActivity(this.intentAppointments);
    	overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    
	public void handleQuit(MenuItem item) {
		quitDialog.show(getFragmentManager(), "QuitDialog");
	}
    
}
