package de.metafinanz.mixnmatch.frontend.android;

import android.app.Activity;
import android.app.ProgressDialog;

public abstract class AbstractAsyncActivity extends Activity {
	
	protected String TAG = "AbstractAsyncActivity";
	private ProgressDialog progressDialog;
	
	public void showProgressDialog() {
		progressDialog = ProgressDialog.show(this, "", "Verarbeite Daten. Bitte warten...", true);
	}
	
	public void dismissProgressDialog() {
		if (progressDialog != null) 
			progressDialog.dismiss();
	}

}
