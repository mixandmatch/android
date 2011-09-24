package de.metafinanz.mixnmatch.frontend.android.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.MMApplication;
import de.metafinanz.mixnmatch.frontend.android.R;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.RessourceType;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;
import de.metafinanz.mixnmatch.frontend.android.data.Request;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;

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
		} else if (intent.getFlags() == FLAG_REQUEST_GET_ONE_REQUEST) {
			Log.d(TAG, "getting one requests - NOT YET IMPLEMENTED");
		} else if (intent.getFlags() == FLAG_REQUEST_POST_REQUEST) {
			Log.d(TAG, "getting all requests");
			String dateAsString = intent.getStringExtra("date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
			Date date = null;
			try {
				date = sdf.parse(dateAsString);
				String userid = intent.getStringExtra("userid");
				String locationKey = intent.getStringExtra("locationKey");
				postRequests(userid, locationKey, date);
			} catch (ParseException e) {
				Log.e(TAG, "Fehlerhaftes Dateumsformat.");
			}
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
			DataServiceHelper.updateLastUpdateTime(RessourceType.Locations);
			Log.d(TAG, "loaded " + locations.length + " items from backend.");
		}
	}
	
	private void postRequests(String userID, String locationKey, Date date) {
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
		Request[] requests = null;
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		restTemplate
				.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
		restTemplate.setMessageConverters(listHttpMessageConverters);

		// The URL for making the GET request
		final String url = getString(R.string.base_uri) +  getString(R.string.uri_requests);

		// Initiate the HTTP GET request, expecting an array of State
		// objects in response
		// Man kann auch gleich ein POJO statt String erstellen lassen.

		Log.d(TAG, "Loading requests from backend...");
		try {
			requests = restTemplate.getForObject(url, Request[].class);

		} catch (Exception e) {
			Log.w(TAG, getText(R.string.error_tech_requestservice).toString(),
					e);
		}
		if (requests != null && requests.length > 0) {
			Log.d(TAG, "Loaded " + requests.length + " items from backend.");
			MMApplication mma = (MMApplication) getApplicationContext();
			mma.setRequests(Arrays.asList(requests));
			
//			getContentResolver().delete(Requests.CONTENT_URI, null, null); // Es kann kein ContentProvider gefunden werden, da in der Manifest-Datei nur Locations geschlüsselt ist.
			for (int i = 0; i < requests.length; i++) {
				ContentValues values = new ContentValues();
				values.put(Requests.LOCATION_KEY, requests[i].getLocationKey());
				values.put(Requests.DATE, requests[i].getDate().toString());
				values.put(Requests.USER_ID, requests[i].getUserid());
				
				Uri uri = getContentResolver().insert(Requests.CONTENT_URI, values);
				Log.d(TAG, "added row '" + uri + "'");
			}
			DataServiceHelper.updateLastUpdateTime(RessourceType.Requests);
		} else {
			Log.d(TAG, "Received no data from backend.");
		}
	}

}
