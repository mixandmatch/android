package de.metafinanz.mixmatch.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public abstract class MixMatchActivity extends Activity {

	private static final String MIXMATCH_PREFS = "mixmatch_preferences";
	private static final String KEY_USERNAME = "mixmatch_username";

	private String username;
	public static final String USER_NAME = "user.name";
	public static final String LOCATION_ID = "locationId";

	@Override
	protected void onResume() {
		super.onResume();
		initUsername();
	}


	public void initUsername() {
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS, MODE_PRIVATE);
		username = settings.getString(KEY_USERNAME, "");
	}

	@Override
	protected void onPause() {
		super.onPause();
		storeUsername();
	}



	public void storeUsername() {
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS, MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putString(KEY_USERNAME, getUsername());
		edit.commit();
		Log.i("Username bei storeUsername: ", "1:" + settings.getString(KEY_USERNAME, ""));
	}


	public String getUsername() {
		return this.username;
	}

	
	public void setUsername(String username) {
		this.username = username;
	}
}
