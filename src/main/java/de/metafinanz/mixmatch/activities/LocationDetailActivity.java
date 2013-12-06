package de.metafinanz.mixmatch.activities;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.service.MixMatchService;

public class LocationDetailActivity extends MixMatchActivity {
	
	private List<Appointment> appointmentList;
	private ArrayAdapter<Appointment> adapter;
	private ListView appointmentsListView;
	private TextView view;
	private Intent intentNewAppointment;
	private String locationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_detail);
		appointmentsListView = (ListView) findViewById(R.id.locationDetailAppointmentsListView);
		intentNewAppointment = new Intent(this, NewAppointmentActivity.class);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		this.locationId = intent.getStringExtra(LOCATION_ID);
		
		view = (TextView) findViewById(R.id.textNameLocationDetail);
		
		Location location = service.getLocationById(locationId);
		
		if (location != null) {
			view.setText(R.string.textLocationDetailHeader);
			getActionBar().setTitle("Location: " + location.getLocationName());
			
			// Asychroner Task für REST-Service
			AsyncTask<Location, Void, List<Appointment>> asyncTask = new AsyncTask<Location, Void, List<Appointment>>() {

				@Override
				protected List<Appointment> doInBackground(Location... params) {
					List<Appointment> list = service.getAppointments(params[0]);
					return list;
				}
				
				@Override
				protected void onPostExecute(List<Appointment> result) {
					super.onPostExecute(result);
					Log.i("LocationActivity", "" + result);
					for (Appointment appointment : result) {
						Log.i("LocationActivityDetail", appointment.getOwner());
					}
					
					if (result.isEmpty()) {
						view.setText(R.string.textLocationDetailHeaderEmptyAppoinments);
					} else {
						appointmentList = result;
						adapter = new MyArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, appointmentList);
						appointmentsListView.setAdapter(adapter);
					}														
				}
			};
			asyncTask.execute(location);			
		}
	}
	
	public void showNewAppointment(View view) {
		intentNewAppointment.putExtra(LOCATION_ID, locationId);
    	startActivity(intentNewAppointment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_detail, menu);
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
			textView.setText(displayTime + " (" + appointment.getOwner() + ")");
			return textView;
		}
	}

}
