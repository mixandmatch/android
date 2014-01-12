package de.metafinanz.mixmatch.activities;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;

public class AppointmentsActivity extends MixMatchActivity {
	
	private List<Appointment> appointmentList;
	private ArrayAdapter<Appointment> adapter;
	private ListView appointmentsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointments);
		appointmentsListView = (ListView) findViewById(R.id.userAppointmentsListView);
		String userName = getUsername();
		TextView text = (TextView) findViewById(R.id.textAppointmentsHeader);
		if (userName != null && userName.length() > 0) {
			text.append(" ");
			text.append(userName);
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getAppointmentsForUser();
	}
	
	private void getAppointmentsForUser() {
		// Asychroner Task für REST-Service
		AsyncTask<String, Void, List<Appointment>> asyncTask = new AsyncTask<String, Void, List<Appointment>>() {

			@Override
			protected List<Appointment> doInBackground(String... params) {
				List<Appointment> list = service.getAppointments(params[0]);
				Collections.sort(list, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment o1, Appointment o2) {
						return o1.getTimestamp().compareTo(o2.getTimestamp());
					}
				});
				return list;
			}

			@Override
			protected void onPostExecute(List<Appointment> result) {
				super.onPostExecute(result);
				Log.i("AppointmentActivity", "" + result);
				for (Appointment appointment : result) {
					Log.i("AppointmentActivity", appointment.getOwner());
				}

				appointmentList = result;
				adapter = new MyArrayAdapter(getApplicationContext(),
						android.R.layout.simple_list_item_1, appointmentList);
				appointmentsListView.setAdapter(adapter);
				
			}
		};
		asyncTask.execute(getUsername());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointments, menu);
		return true;
	}
	
	private class MyArrayAdapter extends ArrayAdapter<Appointment> {
		
		public MyArrayAdapter(Context context, int textViewResourceId,
				List<Appointment> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView textView = (TextView) super.getView(position, convertView, parent);
			textView.setTextColor(getResources().getColor(android.R.color.black));
			Appointment appointment = getItem(position);
			SimpleDateFormat sdf = new SimpleDateFormat(getResources().getText(R.string.dateTimeFormat).toString());
			CharSequence displayTime = sdf.format(appointment.getTimestamp());
			Location location = service.getLocationById(appointment.getLocationID());
			String locationName = location !=null ? location.getLocationName() + ": " : "";
			textView.setText(locationName + displayTime + " (" + appointment.getOwner() + ")");
			return textView;
		}
	}


}
