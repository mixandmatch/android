package de.metafinanz.mixmatch.activities;

import java.util.Calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.User;
import de.metafinanz.mixmatch.service.MixMatchService;

/**
 * @author ulf
 *
 */
public class NewAppointmentActivity extends MixMatchActivity {
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Long locationId;
	private int timePickerHour;
	private int timePickerMinute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar cal = Calendar.getInstance();
		setContentView(R.layout.activity_new_appointment);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(DateFormat.is24HourFormat(this));
		timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));

		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				timePickerHour = hourOfDay;
				timePickerMinute = minute;
				Log.i("Appoinment", "Time changed: " + timePickerHour + ":" + timePickerMinute);
			}
		});
		
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		datePicker.setMinDate(cal.getTime().getTime());
		Intent intent = getIntent();
		this.locationId = intent.getLongExtra(LOCATION_ID, 0);
	}
	
	public void saveAppointment(View view) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentLocation(this.service.getLocationById(locationId));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.AM_PM, Calendar.PM);
		cal.set(Calendar.HOUR_OF_DAY, timePickerHour);
		cal.set(Calendar.MINUTE, timePickerMinute);
		cal.set(Calendar.YEAR, datePicker.getYear());
		cal.set(Calendar.MONTH, datePicker.getMonth());
		cal.set(Calendar.DATE, datePicker.getDayOfMonth());
		
		
		Log.i("Appoinment", "Day of month: " + datePicker.getDayOfMonth() + " Time: " + timePickerHour);
		
		appointment.setOwnerID(new User(getUsername()));
		appointment.setAppointmentDate(cal.getTime());
		
		AsyncTask<Appointment, Void, Void> asyncTask = new AsyncTask<Appointment, Void, Void>() {

			@Override
			protected Void doInBackground(Appointment... params) {
				Appointment appointment = service.createNewAppointment(params[0]);
				if (appointment != null && appointment.getAppointmentID() > 0) {
					// neuen Kalendereintrag erstellen.
				}
				return null;
			}
						
		};
		asyncTask.execute(appointment);
		finish();
		
	}
}
