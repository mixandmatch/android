package de.metafinanz.mixnmatch.frontend.android.services;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.Location;
import de.metafinanz.mixnmatch.frontend.android.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.Position;
import de.metafinanz.mixnmatch.frontend.android.R;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;

public class LocationsService extends Service {
	private static final String TAG = "LocationsService";
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, R.string.location_service_started,
				Toast.LENGTH_LONG).show();
		Log.i(TAG, "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		Log.d(TAG, "onStartCommand");
		startDoings();
		updateLocationsList();
		return START_STICKY;
	}

	private void startDoings() {
		long INTERVAL = 1000;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int count = 0;
			public void run() {
				Log.d(TAG, "Basinga!");
				count++;
				if (count > 3) {
					Log.d(TAG, "got 3");
					stopSelf();
				}
			}
		}, 0, INTERVAL);
		;
	}
	
	private void updateLocationsList() {
		Location[] locations;
		Log.d(TAG, "updateing LocationsList");
		AsyncTask<Void, Void, Location[]> task = new RequestLocation(null).execute();
		try {
			locations = task.get();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Log.d(TAG, "LocationsList updated");
	}

	private void stopDoings() {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, R.string.location_service_created,
				Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, R.string.location_service_stopped,
				Toast.LENGTH_LONG).show();
		stopDoings();
		Log.d(TAG, "onDestroy");
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		LocationsService getService() {
			return LocationsService.this;
		}
	}
	
	private class RequestLocation extends AsyncTask<Void, Void, Location[]> {
		protected String TAG = "RequestLocation";
		
		private Position position;
		
		public RequestLocation(Position position) {
			super();
			this.position = position;
		}
		
		

		@Override
		protected void onPreExecute() {
			// before the network request begins, show a progress indicator
//			showProgressDialog();
		}

		@Override
		protected Location[] doInBackground(Void... params) {
			
			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// The HttpComponentsClientHttpRequestFactory uses the
			// org.apache.http package to make network requests
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

			// The URL for making the GET request
//			final String url = getString(R.string.base_uri) + 
//				"locations/nearby?lat={"+position.getLatitude()+"}&lon={"+position.getLongitude()+"}";
			final String url = getString(R.string.base_uri) + "locations";
			
			// Initiate the HTTP GET request, expecting an array of State
			// objects in response
//			String result = restTemplate.getForObject(url, String.class); 
			//Man kann auch gleich ein POJO statt String erstellen lassen.
			Location[] locations = restTemplate.getForObject(url, Location[].class); 
			
			return locations;
		}

		@Override
		protected void onPostExecute(Location[] locations) {
			// hide the progress indicator when the network request is complete
//			dismissProgressDialog();

			if (locations != null && locations.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < locations.length; i++) {
					sb.append(locations[i].getLabel());
					sb.append("\n");
					ContentValues values = new ContentValues();
					values.put(Locations.KEY, locations[i].getKey());
					values.put(Locations.LABLE, locations[i].getLabel());        
					Uri uri = getContentResolver().insert(Locations.CONTENT_URI, values);
					Log.d(TAG, "added row '"+uri+"'");
				}
				Log.d(TAG, "loaded " + locations.length + "items from backend");
				Toast toast = Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG);
				toast.show();
				

			}
		}
	}

}
