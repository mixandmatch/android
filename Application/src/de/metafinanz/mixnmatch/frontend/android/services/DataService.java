package de.metafinanz.mixnmatch.frontend.android.services;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.Location;
import de.metafinanz.mixnmatch.frontend.android.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.Position;
import de.metafinanz.mixnmatch.frontend.android.R;

/**
 * Serielle Abarbeitung von Intends durch eine interne Queue
 * 
 * @author TSP
 * 
 */
public class DataService extends IntentService {

	private static final String TAG = "DataService";
	
	public DataService() {
		super("DataService");
	}

	/**
	 * Nur zur Ausgabe, dass der Service gestartet wurde.
	 * 
	 * @param intent
	 * @param flags
	 * @param startId
	 * @return
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Toast.makeText(this, R.string.location_service_started,
				Toast.LENGTH_LONG).show();
		Log.i(TAG, "Received start intent: " + intent);
		Log.d(TAG, "onHandleIntent");
		
		if (Location.class.getName().equals(intent.getComponent().getClassName())) {
			Log.d(TAG, "getting locations");
			getLocations();
		}
		// updateLocationsList();
	}

	private void getLocations() {
		Location[] locations = null;
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		restTemplate
				.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		// The URL for making the GET request
		// final String url = getString(R.string.base_uri) +
		// "locations/nearby?lat={"+position.getLatitude()+"}&lon={"+position.getLongitude()+"}";
		final String url = getString(R.string.base_uri) + R.string.uri_locations;

		// Initiate the HTTP GET request, expecting an array of State
		// objects in response
		// String result = restTemplate.getForObject(url, String.class);
		// Man kann auch gleich ein POJO statt String erstellen lassen.
		try {
			locations = restTemplate.getForObject(url, Location[].class);

		} catch (Exception e) {
			Log.w(TAG, getText(R.string.error_tech_locationservice).toString(),
					e);
		}
		if (locations != null && locations.length > 0) {
			getContentResolver().delete(Locations.CONTENT_URI, null, null);
			for (int i = 0; i < locations.length; i++) {
				ContentValues values = new ContentValues();
				values.put(Locations.KEY, locations[i].getKey());
				values.put(Locations.LABLE, locations[i].getLabel());
				values.put(Locations.COORDINATE_LONGITUDE, locations[i]
						.getCoordinates().getLon());
				values.put(Locations.COORDINATE_LADITUDE, locations[i]
						.getCoordinates().getLat());
				Uri uri = getContentResolver().insert(Locations.CONTENT_URI,
						values);
				Log.d(TAG, "added row '" + uri + "'");
			}
			Log.d(TAG, "loaded " + locations.length + " items from backend.");
		}
	}

	// private void updateLocationsList() {
	// Location[] locations;
	// Log.d(TAG, "updateing LocationsList");
	// AsyncTask<Void, Void, Location[]> task = new RequestLocation(null)
	// .execute();
	// try {
	// locations = task.get();
	// if (locations == null || locations.length == 0)
	// Log.d(TAG, "No locations available.");
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }
	// Log.d(TAG, "LocationsList updated");
	// }
	//
	// private class RequestLocation extends AsyncTask<Void, Void, Location[]> {
	// protected String TAG = "RequestLocation";
	//
	// @SuppressWarnings("unused")
	// private Position position;
	//
	// public RequestLocation(Position position) {
	// super();
	// this.position = position;
	// }
	//
	// @Override
	// protected Location[] doInBackground(Void... params) {
	//
	// getLocations();
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Location[] locations) {
	// // hide the progress indicator when the network request is complete
	// // dismissProgressDialog();
	//
	// if (locations != null && locations.length > 0) {
	// StringBuilder sb = new StringBuilder();
	// getContentResolver().delete(Locations.CONTENT_URI, null, null);
	// for (int i = 0; i < locations.length; i++) {
	// sb.append(locations[i].getLabel());
	// sb.append("\n");
	// ContentValues values = new ContentValues();
	// values.put(Locations.KEY, locations[i].getKey());
	// values.put(Locations.LABLE, locations[i].getLabel());
	// Uri uri = getContentResolver().insert(
	// Locations.CONTENT_URI, values);
	// Log.d(TAG, "added row '" + uri + "'");
	// }
	// Log.d(TAG, "loaded " + locations.length + "items from backend");
	// Toast toast = Toast.makeText(getApplicationContext(),
	// sb.toString(), Toast.LENGTH_LONG);
	// toast.show();
	//
	// }
	// }
	// }

}
