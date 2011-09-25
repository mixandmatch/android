package de.metafinanz.mixnmatch.frontend.android.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.metafinanz.mixnmatch.frontend.android.MMApplication;
import de.metafinanz.mixnmatch.frontend.android.R;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;
import de.metafinanz.mixnmatch.frontend.android.data.Request;

/**
 * Serielle Abarbeitung von Intends durch eine interne Queue
 * 
 * @author TSP
 * 
 */
public class DataService extends IntentService {

	private static final String TAG = "DataService";

	public final static int FLAG_REQUEST_UPDATE_LOCATIONS = 100;
	public final static int FLAG_REQUEST_GET_ALL_REQUEST = 200;
	public final static int FLAG_REQUEST_GET_ONE_REQUEST = 300;
	public final static int FLAG_REQUEST_POST_REQUEST = 400;

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

		if (intent.getFlags() == FLAG_REQUEST_UPDATE_LOCATIONS) {
			Log.d(TAG, "getting locations");
			getLocations();
		} else if (intent.getFlags() == FLAG_REQUEST_GET_ALL_REQUEST) {
			Log.d(TAG, "getting all requests");
			getAllRequests();
		} else if (intent.getFlags() == FLAG_REQUEST_POST_REQUEST) {
			Log.d(TAG, "posting request");
			String date = intent.getStringExtra("date");
			String userid = intent.getStringExtra("userid");
			String locationKey = intent.getStringExtra("locationKey");
			postRequests(userid,locationKey, date);
		}
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
		final String url = getString(R.string.base_uri) +  getString(R.string.uri_locations);

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
	
	private void postRequests(String userID, String locationKey, String date) {
		final String url = getString(R.string.base_uri) + getString(R.string.uri_requests);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application","json"));
		
		Request req = new Request(locationKey, date, userID);
		HttpEntity<Request> requestEntity = new HttpEntity<Request>(req, requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> resultURL = null;
		try {
			resultURL = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		} catch (Exception e) {
			Log.e(TAG, "Fehler beim senden des Requests", e);
		}
		
		if (resultURL != null && resultURL.getBody() != null) {
			Log.d(TAG, "Posted data to backend. Result: " + resultURL.getBody());
		} else {
			Log.d(TAG, "Received no data from backend.");
		}
	}
	
	private void getAllRequests() {
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		restTemplate
				.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		// The URL for making the GET request
		final String requestUrl = getString(R.string.base_uri) +  getString(R.string.uri_requests);

		// Initiate the HTTP GET request, expecting an array of State
		// objects in response
		// Man kann auch gleich ein POJO statt String erstellen lassen.
		List<Request> listRequests = new ArrayList<Request>();
		try {
			String json = restTemplate.getForObject(requestUrl, String.class);
			Gson gson = new Gson();
			List<Map<String, String>> listJSONobjects = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {}.getType());
			
			int countReqAdded = 0;
			Log.i(TAG, "Received " + listJSONobjects.size() + " request objects from backend.");
			for (Map<String, String> mapData : listJSONobjects) {
				Request req = new Request(mapData);
				getContentResolver().insert(Requests.CONTENT_URI, req.getContentValues());
				listRequests.add(req);
				countReqAdded++;
			}
			Log.d(TAG, "Loaded " + countReqAdded + " items from backend.");

		} catch (Exception e) {
			Log.w(TAG, getText(R.string.error_tech_requestservice).toString(), e);
		}
	}

}
