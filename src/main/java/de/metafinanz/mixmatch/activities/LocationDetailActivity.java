package de.metafinanz.mixmatch.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_detail);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		String locationId = intent.getStringExtra(LOCATION_ID);
		TextView view = (TextView) findViewById(R.id.textNameLocationDetail);
		
		Location location = MixMatchService.getInstance().getLocationById(locationId);
		
		if (location != null) {
			view.setText(R.string.textLocationDetailHeader);
			getActionBar().setTitle("Location: " + location.getLocationName());
			
			List<Appointment> appointments = MixMatchService.getInstance().getAppointments(location);
			
			if (appointments.isEmpty()) {
				view.setText(R.string.textLocationDetailHeaderEmptyAppoinments);
			}
			
			ArrayAdapter<Appointment> adapter = new ArrayAdapter<Appointment>(getApplicationContext(), android.R.layout.simple_list_item_1, appointments) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					TextView textView = (TextView) super.getView(position, convertView, parent);
					textView.setTextColor(getResources().getColor(android.R.color.black));
					Appointment appointment = getItem(position);					
					CharSequence displayTime = DateFormat.format(getResources().getText(R.string.dateTimeFormat), appointment.getTimestamp());
					textView.setText(displayTime + " (" + appointment.getOwner().getUsername() + ")");
					return textView;
				}
			};
			
			ListView appointmentsListView = (ListView) findViewById(R.id.locationDetailAppointmentsListView);
			appointmentsListView.setAdapter(adapter);
			
		} 
		
		
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
