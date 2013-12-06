package de.metafinanz.mixmatch.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.metafinanz.mixmatch.R;

public class AppointmentsActivity extends MixMatchActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointments);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String userName = getUsername();
		TextView text = (TextView) findViewById(R.id.textAppointmentsHeader);
		if (userName != null && userName.length() > 0) {
			text.append(userName);			
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointments, menu);
		return true;
	}


}
