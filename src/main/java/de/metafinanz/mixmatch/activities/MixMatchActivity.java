package de.metafinanz.mixmatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import de.metafinanz.mixmatch.service.MixMatchService;

public abstract class MixMatchActivity extends Activity {

	public static final String USER_NAME = "user.name";
	public static final String LOCATION_ID = "locationId";
	public static final String APPOINTMENT_ID = "appointmentId";
	
	public static final String MIXMATCH_PREFS = "mixmatch_preferences";
	public static final String KEY_USERNAME = "mixmatch_username";

	private String username;
	
	protected MixMatchService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		service = MixMatchService.getInstance(getBaseContext());
		setupActionBar();
		initUsername();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		initUsername();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initUsername();
	}

	public void showSettings(MenuItem item) {
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivityForResult(intent, 1);
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
	
	protected void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void storeUsername() {
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS, MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putString(KEY_USERNAME, getUsername());
		edit.commit();
		Log.i("Username bei storeUsername: ", "1:" + settings.getString(KEY_USERNAME, ""));
	}
	
	public void storeString(String key, String value) {
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS, MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public String getString(String key) {
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS, MODE_PRIVATE);
		String value = settings.getString(key, key);
		return value;
	}

	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
		storeUsername();
	}
	
}
