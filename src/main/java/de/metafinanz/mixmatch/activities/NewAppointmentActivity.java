package de.metafinanz.mixmatch.activities;

import java.util.Calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.User;

/**
 * @author ulf
 *
 */
public class NewAppointmentActivity extends MixMatchActivity {
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	private String locationId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_appointment);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		datePicker.setMinDate(cal.getTime().getTime());
		Intent intent = getIntent();
		this.locationId = intent.getStringExtra(LOCATION_ID);
	}
	
	public void saveAppointment(View view) {
		Appointment appointment = new Appointment();
		appointment.setLocationID(locationId);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, timePicker.getCurrentHour());
		cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		cal.set(Calendar.YEAR, datePicker.getYear());
		cal.set(Calendar.MONTH, datePicker.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		
		appointment.setOwner(getUsername() == null ? "ulf" : getUsername());
		appointment.setTimestamp(cal.getTime());
		
		AsyncTask<Appointment, Void, Void> asyncTask = new AsyncTask<Appointment, Void, Void>() {

			@Override
			protected Void doInBackground(Appointment... params) {
				String appointmentID = service.createNewAppointment(params[0]);
				if (appointmentID != null && appointmentID.length() > 0) {
					// neuen Kalendereintrag erstellen.
				}
				return null;
			}
						
		};
		asyncTask.execute(appointment);
		finish();
		
	}
}
