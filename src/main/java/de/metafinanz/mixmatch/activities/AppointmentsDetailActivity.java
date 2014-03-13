package de.metafinanz.mixmatch.activities;

import java.text.SimpleDateFormat;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;

public class AppointmentsDetailActivity extends MixMatchActivity {

	private Long appointmentId;
	private Appointment appointment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointments_detail);
		getAppointmentDetails();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
	        upIntent.putExtra(LOCATION_ID, appointment.getAppointmentLocation().getLocationID());
	        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
	            // This activity is NOT part of this app's task, so create a new task
	            // when navigating up, with a synthesized back stack.
	            TaskStackBuilder.create(this)
	                    // Add all of this activity's parents to the back stack
	                    .addNextIntentWithParentStack(upIntent)
	                    // Navigate up to the closest parent
	                    .startActivities();
	        } else {
	            // This activity is part of this app's task, so simply
	            // navigate up to the logical parent activity.
	            NavUtils.navigateUpTo(this, upIntent);
	        }
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private void getAppointmentDetails() {
		this.appointmentId = getIntent().getLongExtra(APPOINTMENT_ID, 0);
		
		AsyncTask<Long, Void, Appointment> task = new AsyncTask<Long, Void, Appointment>() {
			@Override
			protected Appointment doInBackground(Long... params) {
				appointment = service.getAppointmentById(params[0]);
				return appointment;
			}
			@Override
			protected void onPostExecute(Appointment app) {
				super.onPostExecute(app);
				SimpleDateFormat sdf = new SimpleDateFormat(getResources().getText(R.string.dateTimeFormat).toString());
				CharSequence displayTime = sdf.format(app.getAppointmentDate());
				((TextView) findViewById(R.id.textAppointmentDetailOwnerValue)).setText(app.getOwnerID().getUsername());
				((TextView) findViewById(R.id.textAppointmentDetailDateValue)).setText(displayTime);
			}
		};
		
		task.execute(appointmentId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointments_detail, menu);
		return true;
	}

}
