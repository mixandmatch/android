package de.metafinanz.mixnmatch.frontend.android.services;

import de.metafinanz.mixnmatch.frontend.android.Location;
import de.metafinanz.mixnmatch.frontend.android.Location.Locations;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

		Log.d(TAG, "starting service");
		Intent requestIntent = new Intent(context, Location.class);
		context.startService(requestIntent);
	}

}
