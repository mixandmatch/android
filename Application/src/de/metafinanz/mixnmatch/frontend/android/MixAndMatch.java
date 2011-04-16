package de.metafinanz.mixnmatch.frontend.android;

import de.metafinanz.mixnmatch.frontend.android.services.LocationsService;
import de.metafinanz.mixnmatch.frontend.android.services.LocationsServiceHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MixAndMatch extends Activity {
	private Intent iRequestMatch;
	private static final String TAG = "MixAndMatch";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Toast toast = Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT);
		toast.show();
        
        iRequestMatch = new Intent(this, RequestMatch.class);
        
        Button btnMatch = (Button) findViewById(R.id.buttonMatches);
        OnClickListener oclBtnMatches = new OnClickListener() {
			public void onClick(View v) {
				startActivity(iRequestMatch);
			}
		};
		btnMatch.setOnClickListener(oclBtnMatches);

		LocationsServiceHelper.getInstance(this).updateLocations();
    }

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "stopping service");
	    stopService(new Intent(this, LocationsService.class));
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "restarting service");
	    startService(new Intent(this, LocationsService.class));
	}
}