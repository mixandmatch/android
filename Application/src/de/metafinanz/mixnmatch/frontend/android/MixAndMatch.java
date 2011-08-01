package de.metafinanz.mixnmatch.frontend.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.services.DataService;
import de.metafinanz.mixnmatch.frontend.android.services.DataServiceHelper;

public class MixAndMatch extends Activity {
	private static final String USER_E_MAIL = "userEMail";
	private static final String USER_NAME = "userName";
	private static final String USER_ID = "userID";
	private Intent iRequestMatch;
	private Intent iReceiveMatch;
	private Intent iMyRequests;
	private static final String TAG = "MixAndMatch";
	private static final int DIALOG_SETTINGS_ID = 100;



	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        DataServiceHelper.getInstance(this).updateLocations();
        DataServiceHelper.getInstance(this).getRequests();
        
        Toast toast = Toast.makeText(getApplicationContext(), "Willkommen bei Mix'n'Match", Toast.LENGTH_SHORT);
		toast.show();
        
		iRequestMatch = new Intent(this, RequestMatch.class);
        iReceiveMatch = new Intent(this, ReceiveMatch.class);
        iMyRequests = new Intent(this, MyRequests.class);
        
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
		
		Button btnMeineAnfragen = (Button) findViewById(R.id.buttonMeineAnfragen);
        OnClickListener oclBtnMeineAnfragen = new OnClickListener() {
			public void onClick(View v) {
				startActivity(iMyRequests);
			}
		};
		btnMeineAnfragen.setOnClickListener(oclBtnMeineAnfragen);
		
		init();

		MMApplication mma = (MMApplication) getApplicationContext();
		if (mma.getUserID() == null || mma.getUserEMail() == null || mma.getUserName() == null) {
			showDialog(DIALOG_SETTINGS_ID);
		}
		
//		DataServiceHelper.getInstance(this).postRequest("HVU" , new Date());
    }

	@Override
	protected void onResume() {
		super.onStart();
	}

	private void init() {
		// Restore preferences
	    SharedPreferences settings = getPreferences(MODE_PRIVATE);
		MMApplication mma = (MMApplication) getApplicationContext();
	    mma.setUserID(settings.getString(USER_ID, null));
	    mma.setUserName(settings.getString(USER_NAME, null));
	    mma.setUserEMail(settings.getString(USER_E_MAIL, null));

		Log.i(TAG, "User ID:" + mma.getUserID());
		Log.i(TAG, "User Name:" + mma.getUserName());
		Log.i(TAG, "User EMail:" + mma.getUserEMail());
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "stopping service");
	    stopService(new Intent(this, DataService.class));
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "restarting service");
	    startService(new Intent(this, DataService.class));
	}
	
	protected Dialog onCreateDialog(int id) {
	    final Dialog dialog;
//	    MMApplication mma = (MMApplication) getApplicationContext();
	    switch(id) {
	    case DIALOG_SETTINGS_ID:
	    	dialog = new Dialog(this);

			dialog.setContentView(R.layout.dialog_settings);
			dialog.setTitle("Einstellungen");
			
			Button btnSave = (Button)dialog.findViewById(R.id.btnSave);
			btnSave.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					EditText editUserId = (EditText) dialog.findViewById(R.id.editUserId);
					EditText editName = (EditText) dialog.findViewById(R.id.editName);
					EditText editEmail = (EditText) dialog.findViewById(R.id.editEMail);
					
					if (editName.getText().toString() == null || editName.getText().toString().length() == 0) {
				        Toast toast = Toast.makeText(getApplicationContext(), "Bitte einen Namen eingeben", Toast.LENGTH_SHORT);
				        toast.show();
					} else if (editUserId.getText().toString() == null || editUserId.getText().toString().length() == 0) {
				        Toast toast = Toast.makeText(getApplicationContext(), "Bitte eine User-ID eingeben", Toast.LENGTH_SHORT);
				        toast.show();
					}else if (editEmail.getText().toString() == null || editEmail.getText().toString().length() == 0) {
				        Toast toast = Toast.makeText(getApplicationContext(), "Bitte eine EMail-Adresse eingeben", Toast.LENGTH_SHORT);
				        toast.show();
					} else {
					    MMApplication mma = (MMApplication) getApplicationContext();
					    mma.setUserID(editUserId.getText().toString());
					    mma.setUserName(editName.getText().toString());
					    mma.setUserEMail(editEmail.getText().toString());
					    
						SharedPreferences settings = getPreferences(MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString(USER_ID, mma.getUserID());
						editor.putString(USER_NAME, mma.getUserName());
						editor.putString(USER_E_MAIL, mma.getUserEMail());
						
						// Commit the edits!
						editor.commit();
						
						dismissDialog(DIALOG_SETTINGS_ID);
					}
				}
			});
	        break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
}