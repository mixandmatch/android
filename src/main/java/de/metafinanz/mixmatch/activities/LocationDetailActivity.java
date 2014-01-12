package de.metafinanz.mixmatch.activities;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.activities.dialogs.AppointmentAlertDialogFragment;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;

/**
 * Activity, die zu einer Location die verfügbaren Verabredungen anzeigt.
 * @author ulf
 *
 */
public class LocationDetailActivity extends MixMatchActivity {
	
	private List<Appointment> appointmentList;
	private ArrayAdapter<Appointment> adapter;
	private ListView appointmentsListView;
	private TextView view;
	private Intent intentNewAppointment;
	private String locationId;
	private AppointmentAlertDialogFragment appointmentAlertDialogFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_detail);
		appointmentsListView = (ListView) findViewById(R.id.locationDetailAppointmentsListView);
		intentNewAppointment = new Intent(this, NewAppointmentActivity.class);
		appointmentAlertDialogFragment = new AppointmentAlertDialogFragment();
	}
	
	public void showAlertDialog() {
		appointmentAlertDialogFragment.show(getFragmentManager(), "ALertDialog");
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
			
			appointmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long id) {
					Appointment location = (Appointment) parent.getItemAtPosition(position);
					//intentLocationDetail.putExtra(LOCATION_ID, location.getLocationID());
					//startActivity(intentLocationDetail);				
				}
			});
			appointmentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View arg1,
						int position, long id) {
					showAlertDialog();
					return false;
				}
			});
		}
	}
	
	public void showNewAppointment(View view) {
		intentNewAppointment.putExtra(LOCATION_ID, locationId);
    	startActivityForResult(intentNewAppointment, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
