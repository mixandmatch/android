package de.metafinanz.mixmatch.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.service.MixMatchService;

public class LocationsActivity extends MixMatchActivity {
	
	private Intent intentLocationDetail;
	private List<Location> locationList = new ArrayList<Location>();
	private ListView view;
	private ArrayAdapter<Location> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		intentLocationDetail = new Intent(this, LocationDetailActivity.class);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		view = (ListView) findViewById(R.id.locationsListView);
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
				Log.i("LocationActivity", result.toString());
				for (Location location : result) {
					Log.i("LocationActivity", location.getLocationName());
				}
				locationList = result;
				adapter = new MyArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, locationList);
				view.setAdapter(adapter);				
			}
			
		};
		
		asyncTask.execute();
		
		adapter = new MyArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, locationList);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) {
				Location location = (Location) parent.getItemAtPosition(position);
				intentLocationDetail.putExtra(LOCATION_ID, location.getLocationID());
				startActivity(intentLocationDetail);				
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locations, menu);
		return true;
	}

	
	private class MyArrayAdapter extends ArrayAdapter<Location> {
		
		public MyArrayAdapter(Context context, int textViewResourceId,
				List<Location> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = (TextView) super.getView(position, convertView, parent);
			textView.setTextColor(getResources().getColor(android.R.color.black));				
			return textView;
		}
	}

}
