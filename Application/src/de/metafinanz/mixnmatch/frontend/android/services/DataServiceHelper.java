package de.metafinanz.mixnmatch.frontend.android.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.MMApplication;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.RessourceType;

public class DataServiceHelper {
    private static final String TAG = "DataServiceHelper";
	private static final int MIN_UPDATE_INTERVALL = 5; //Minuten
    private Context context;
    private static Map<RessourceType, Calendar> mapUpdates = new HashMap<RessourceType, Calendar>();
    
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
	 * Überprüfung, ob ein Update durchgeführt werden soll.
	 * @param force
	 * @return
	 */
	private boolean canUpdate(boolean force, RessourceType type) {
		if (force)  {
			Log.i(TAG, "Data update forced.");
			return true;
		}
		
		Calendar actualTime = GregorianCalendar.getInstance();
		actualTime.add(Calendar.MINUTE, MIN_UPDATE_INTERVALL * (-1));
		if (mapUpdates.get(type) == null) {
			Log.i(TAG, "lastUpdateTime is null - updating...");
			return true;
		}
		if (mapUpdates.get(type).before(actualTime)) {
			Log.i(TAG, "lastUpdateTime older than 5 minutes - updating...");
			return true;
		}
		return false;
	}

	static void updateLastUpdateTime(RessourceType type) {
		Calendar lastUpdateTime = GregorianCalendar.getInstance();
		mapUpdates.put(type, lastUpdateTime);
	}
	
	
	/**
	 * Aktualisieren der Lokationen
	 */
	public void updateLocations()  {
		updateLocations(false);
	}
	public void updateLocations(boolean force)  {
		if (canUpdate(force, RessourceType.Locations)) {
			Log.d(TAG, "starting service for updateLocations()");
			Intent requestIntent = new Intent(context, DataService.class);
			requestIntent.addFlags(DataService.FLAG_REQUEST_UPDATE_LOCATIONS);
			context.startService(requestIntent);
		}
	}

	/**
	 * URI: BASE_URI/requests 	
	 * HTTP Method: GET 	
	 * Beschreibung: Listet alle jemals geposteten Requests auf.
	 */
	public void getRequests() {
		getRequests(false);
	}
	public void getRequests(boolean force) {
		if (canUpdate(force, RessourceType.Requests)) {
			Log.d(TAG, "starting service for getRequests()");
			Intent requestIntent = new Intent(context, DataService.class);
			requestIntent.addFlags(DataService.FLAG_REQUEST_GET_ALL_REQUEST);
	
			context.startService(requestIntent);
			updateLastUpdateTime(RessourceType.Requests);
		}
	}
	
	/**
	 * URI: BASE_URI/requests/{location}/{date}/lunch/{user} 	
	 * HTTP Method: GET 	
	 * Beschreibung: Liefert u.g. JSON Objekt, wenn zuvor ein POST auf BASE_URI/requests gemacht wurde.
	 */
	public void getRequest(String userID, Location location, Date date) {
		getRequest(userID, location, date, false);
	}
	public void getRequest(String userID, Location location, Date date, boolean force) {
		Log.d(TAG, "starting service for getRequest(String userID, Location location, Date date)");
		Intent requestIntent = new Intent(context, DataService.class);
		requestIntent.addFlags(DataService.FLAG_REQUEST_GET_ONE_REQUEST);
		context.startService(requestIntent);
	}
	
	/**
	 * URI: BASE_URI/requests 	
	 * HTTP Method: POST 	
	 * Beschreibung: Ein Post auf diese Resource erzeugt ein neues Request Objekt. Im Request Body muss u.g. Event Request JSON Objekt mitgeliefert werden. Anschließend erfolgt ein Redirect auf die soeben erzeugte Resource.
	 */
	public void postRequest(String selectedPlace, Date date) {
		Log.d(TAG, "starting service for postRequest(String userID, Location location, Date date)");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		MMApplication app = (MMApplication) context.getApplicationContext();
		
		Intent requestIntent = new Intent(context, DataService.class);
		requestIntent.addFlags(DataService.FLAG_REQUEST_POST_REQUEST);
		Bundle dataBundle = new Bundle();
		dataBundle.putString("date", sdf.format(date));
		dataBundle.putString("locationKey", selectedPlace);
		dataBundle.putString("userid", app.getUserID());
		requestIntent.putExtras(dataBundle);
		context.startService(requestIntent);
	}

}
