package de.metafinanz.mixmatch.activities;

import de.metafinanz.mixmatch.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ImpressActivity extends MixMatchActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impress);
		// Show the Up button in the action bar.
		setupActionBar();
	}

}
