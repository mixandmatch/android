package de.metafinanz.mixnmatch.frontend.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MixAndMatch extends Activity {
	private Intent iRequestMatch;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Toast toast = Toast.makeText(getApplicationContext(), "Willkommen bei Mix'n'Match", Toast.LENGTH_SHORT);
		toast.show();
        
        iRequestMatch = new Intent(this, RequestMatch.class);
        
        Button btnMatch = (Button) findViewById(R.id.buttonMatchwunschBekanntgeben);
        OnClickListener oclBtnMatches = new OnClickListener() {
			public void onClick(View v) {
				startActivity(iRequestMatch);
			}
		};
		btnMatch.setOnClickListener(oclBtnMatches);
    }
}