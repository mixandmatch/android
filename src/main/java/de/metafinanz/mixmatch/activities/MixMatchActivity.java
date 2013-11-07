package de.metafinanz.mixmatch.activities;

import android.app.Activity;
import android.content.SharedPreferences;

public abstract class MixMatchActivity extends Activity {

	private static final String MIXMATCH_PREFS = "mixmatch_preferences";
	private static final String KEY_USERNAME = "mixmatch_username";

	private String username;

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS,
				MODE_PRIVATE);
		username = settings.getString(KEY_USERNAME, "");
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences(MIXMATCH_PREFS,
				MODE_PRIVATE);
		settings.edit().putString(KEY_USERNAME, username);
		settings.edit().commit();
	}

	protected String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}
}
