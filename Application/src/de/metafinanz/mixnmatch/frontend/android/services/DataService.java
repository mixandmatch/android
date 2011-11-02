package de.metafinanz.mixnmatch.frontend.android.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.metafinanz.mixnmatch.frontend.android.R;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.data.Request;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;

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
	public final static int FLAG_REQUEST_POST_ONE_REQUEST = 400;
	public final static int FLAG_REQUEST_DELETE_ONE_REQUEST = 500;
	public final static int FLAG_REQUEST_GET_ALL_REQUEST_AT_LOCATION = 600;
	public final static int FLAG_REQUEST_GET_ALL_REQUEST_AT_LOCATION_AT_DATE = 700;

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
		} else if (intent.getFlags() == FLAG_REQUEST_GET_ALL_REQUEST_AT_LOCATION) {
			Log.d(TAG, "getting all requests at a specific location");
			getAllRequests();
		} else if (intent.getFlags() == FLAG_REQUEST_GET_ALL_REQUEST_AT_LOCATION_AT_DATE) {
			Log.d(TAG, "getting all requests at a specific location at a specific date");
			getAllRequests();
		} else if (intent.getFlags() == FLAG_REQUEST_GET_ONE_REQUEST) {
			Log.d(TAG, "getting one requests - NOT YET IMPLEMENTED");
			getSpecificRequest();
		} else if (intent.getFlags() == FLAG_REQUEST_DELETE_ONE_REQUEST) {
			Log.d(TAG, "deleting one requests - NOT YET IMPLEMENTED");
			deleteRequest(intent);
		} else if (intent.getFlags() == FLAG_REQUEST_POST_ONE_REQUEST) {
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
		Log.d(TAG, "Requesting URL: " + url);
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
				values.put(Locations.DESCRIPTION, locations[i].getDescription());
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

	/**
	 * EventRequest-Objekt welches gefüllt werden muss.
	 * { 
	 *   "locationKey": "<JSON String, required>",
	 *   "date": "<JSON String, required>",
	 *   "userid": <JSON String, required>",
	 *   "url": "<JSON String, wird vom Webservice erzeugt>",
	 *   "matchUrl": "<JSON String, nur bei /users/user s.o.>"
	 *}
	 *
	 * curl -i -v -H "Accept: application/json" -X POST -H "Content-Type: application/json" -d '{"locationKey":"hvu","date":"20110401","userid":"bad"}' http://mixmatch-t.elasticbeanstalk.com/requests
	 *
	 * Method: post
	 * @param userID
	 * @param locationKey
	 * @param date
	 */
	private void postRequests(String userID, String locationKey, String date) {
		final String url = getString(R.string.base_uri) + getString(R.string.uri_requests);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		Request req = new Request(locationKey, date, userID);
		Gson gson = new Gson();
		String resGson = gson.toJson(req.getDataAsMap());
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(resGson, requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> resultURL = null;
		try {
//			resultURL = restTemplate.postForEntity(url,  requestEntity, String.class); 
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

	/**
	 * URL: /requests
	 * Method: get
	 */
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

	/**
	 * URL: /requests/{location}/{date}/lunch/{user}
	 * Method: delete
	 */
	private void deleteRequest(Intent intent) {
		String date = intent.getStringExtra("date");
		String user = intent.getStringExtra("userid");
		String locationKey = intent.getStringExtra("locKey");
		
		if (date == null || user == null) {
			Log.e(TAG, "Can't delete Request. Date or user ist null.");
			return;
		}
		StringBuffer url = new StringBuffer();
		url.append(getString(R.string.base_uri));
		url.append(getString(R.string.uri_requests));
		url.append("/");
		url.append(locationKey);
		url.append("/");
		url.append(date);
		url.append("/lunch/");
		url.append(user);
		Log.i(TAG, "Lösche mit URL " + url);
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(url.toString());
		
	}
	
	/**
	 * URL: /{location}/{date}/lunch/{user}
	 * Method: get
	 */
	private void getSpecificRequest() {
		
	}
	
	/**
	 * URL: /{location}
	 * Method: get
	 */
	private void getRequestsAtLocation() {
		
		
	}
	
	/**
	 * URL: /{location}/{date}
	 * 		/{location}/{date}/lunch
	 * Method: get
	 */
	private void getRequestsAtLocationAtDate() {
		
		
	}
}
