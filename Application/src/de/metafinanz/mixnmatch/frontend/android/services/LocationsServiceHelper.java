package de.metafinanz.mixnmatch.frontend.android.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.R;

public class LocationsServiceHelper {
    private static final String TAG = "LocationsServiceHelper";
    private Context context;

	private LocationsServiceHelper() {
	}

	/**
	 * ContentLocationsHolder is loaded on the first execution of
	 * ContentLocations.getInstance() or the first access to ContentLocationsHolder.INSTANCE,
	 * not before.
	 */
	private static class LocationsServiceHelperHolder {
		public static final LocationsServiceHelper INSTANCE = new LocationsServiceHelper();
	}
	
	private void setContext(Context context) {
		this.context = context;
	}
	
	public static LocationsServiceHelper getInstance(Context context) {
		if (LocationsServiceHelperHolder.INSTANCE != null) {
			LocationsServiceHelperHolder.INSTANCE.setContext(context.getApplicationContext()) ;
		}
		return LocationsServiceHelperHolder.INSTANCE;
	}
	
	
	/**
	 * – Check if the method is already pending
	 * – Create the request Intent
	 * – Add the operation type and a unique request id //not needed
	 * – Add the method specific parameters //not needed
	 * – Add the binder callback
	 * – Call startService(Intent)
	 * – Return the request id
	 */
	public void updateLocations()  {

		Log.d(TAG, "starting service");
		Intent requestIntent = new Intent(context, LocationsService.class);
		context.startService(requestIntent);
	}

}
