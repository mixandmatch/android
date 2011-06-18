package de.metafinanz.mixnmatch.frontend.android.services;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Request;

public class DataServiceHelper {
    private static final String TAG = "DataServiceHelper";
    private Context context;

	private DataServiceHelper() {
	}

	/**
	 * ContentLocationsHolder is loaded on the first execution of
	 * ContentLocations.getInstance() or the first access to ContentLocationsHolder.INSTANCE,
	 * not before.
	 */
	private static class LocationsServiceHelperHolder {
		public static final DataServiceHelper INSTANCE = new DataServiceHelper();
	}
	
	private void setContext(Context context) {
		this.context = context;
	}
	
	public static DataServiceHelper getInstance(Context context) {
		if (LocationsServiceHelperHolder.INSTANCE != null) {
			LocationsServiceHelperHolder.INSTANCE.setContext(context.getApplicationContext()) ;
		}
		return LocationsServiceHelperHolder.INSTANCE;
	}
	
	
	/**
	 * Aktualisieren der Lokationen
	 */
	public void updateLocations()  {
		Log.d(TAG, "starting service for updateLocations()");
		Intent requestIntent = new Intent(context, DataService.class);
		requestIntent.addFlags(DataService.FLAG_REQUEST_UPDATE_LOCATIONS);
		context.startService(requestIntent);
	}
	
	/**
	 * URI: BASE_URI/requests 	
	 * HTTP Method: GET 	
	 * Beschreibung: Listet alle jemals geposteten Requests auf.
	 */
	public void getRequests() {
		Log.d(TAG, "starting service for getRequests()");
		Intent requestIntent = new Intent(context, DataService.class);
		requestIntent.addFlags(DataService.FLAG_REQUEST_GET_ALL_REQUEST);

		context.startService(requestIntent);
	}
	
	/**
	 * URI: BASE_URI/requests/{location}/{date}/lunch/{user} 	
	 * HTTP Method: GET 	
	 * Beschreibung: Liefert u.g. JSON Objekt, wenn zuvor ein POST auf BASE_URI/requests gemacht wurde.
	 */
	public void getRequest(String userID, Location locaation, Date date) {
		Log.d(TAG, "starting service for getRequest(String userID, Location location, Date date)");
		Intent requestIntent = new Intent(context, DataService.class);
		requestIntent.addFlags(DataService.FLAG_REQUEST_GET_ONE_REQUEST);
		context.startService(requestIntent);
	}
	
	/**
	 * URI: BASE_URI/requests 	
	 * HTTP Method: POST 	
	 * Beschreibung: Ein Post auf diese Resource erzeugt ein neues Request Objekt. Im Request Body muss u.g. Event Request JSON Objekt mitgeliefert werden. Anschlieﬂend erfolgt ein Redirect auf die soeben erzeugte Resource.
	 */
	public void postRequest(String userID, Location locaation, Date date) {
		Log.d(TAG, "starting service for postRequest(String userID, Location location, Date date)");
		Intent requestIntent = new Intent(context, DataService.class);
		requestIntent.addFlags(DataService.FLAG_REQUEST_POST_REQUEST);
		context.startService(requestIntent);
	}

}
