package de.metafinanz.mixnmatch.frontend.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.services.LocationsService;
import de.metafinanz.mixnmatch.frontend.android.services.LocationsServiceHelper;

public class MixAndMatch extends Activity {
	private Intent iRequestMatch;
	private Intent iReceiveMatch;
	private static final String TAG = "MixAndMatch";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Toast toast = Toast.makeText(getApplicationContext(), "Willkommen bei Mix'n'Match", Toast.LENGTH_SHORT);
		toast.show();
        
		iRequestMatch = new Intent(this, RequestMatch.class);
        iReceiveMatch = new Intent(this, ReceiveMatch.class);
        
        Button btnMatchBekannt = (Button) findViewById(R.id.buttonMatchwunschBekanntgeben);
        OnClickListener oclBtnMatchesBekannt = new OnClickListener() {
			public void onClick(View v) {
				startActivity(iRequestMatch);
			}
		};
		btnMatchBekannt.setOnClickListener(oclBtnMatchesBekannt);
		
		Button btnMatchEmpfangen = (Button) findViewById(R.id.buttonMatchEmpfangen);
        OnClickListener oclBtnMatchesEmpfangen = new OnClickListener() {
			public void onClick(View v) {
				startActivity(iReceiveMatch);
			}
		};
		btnMatchEmpfangen.setOnClickListener(oclBtnMatchesEmpfangen);

//		LocationsServiceHelper.getInstance(this).updateLocations();
    }

//	@Override
//	protected void onStop() {
//		super.onStop();
//		Log.d(TAG, "stopping service");
//	    stopService(new Intent(this, LocationsService.class));
//	}
//	
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		Log.d(TAG, "restarting service");
//	    startService(new Intent(this, LocationsService.class));
//	}
}