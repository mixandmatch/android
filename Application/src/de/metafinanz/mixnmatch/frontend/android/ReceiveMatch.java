package de.metafinanz.mixnmatch.frontend.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ReceiveMatch extends Activity implements OnTouchListener {
	
	private ViewFlipper viewFlipper;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_match);
		viewFlipper = (ViewFlipper)findViewById(R.id.Flipper);
		
		gestureDetector = new GestureDetector(new GestureListener(this, viewFlipper));
		gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
		
	}
	
//	mName  = (TextView) findViewById(R.id.AnzeigePartnerName);
//	mEmail = (TextView) findViewById(R.id.AnzeigePartnerEmail);
//	mOrt   = (TextView) findViewById(R.id.AnzeigePartnerOrt);
//	mDatum = (TextView) findViewById(R.id.AnzeigeDatumTreff);
//	mZeit  = (TextView) findViewById(R.id.AnzeigeZeitTreff);
//	
//	mName.setText("Hans Wurst");
//	mEmail.setText("hans.wurst@metzger.de");
//	mOrt.setText("Metzgerei Dimpflmoser");
//	mDatum.setText("02.05.2011");
//	mZeit.setText("12:00");
//	
//	Button btnEmailSenden = (Button) findViewById(R.id.buttonEmailSenden);
//    OnClickListener oclBtnEmailSenden = new OnClickListener() {
//		public void onClick(View v) {
//			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//			
//			String EmailSubject = new String();
//			String EmailText = new String();
//			
//			EmailSubject = new StringBuilder().append("Unser Mix and Match Treffen am ").append(mDatum).toString();
//			
//			EmailText = new StringBuilder().append("Lieber ").append(mName).append(", " +
//					"wir wurden so eben ausgelost für ein gemeinsames Mittagessen." +
//					"Deswegen wollte ich dir nur nochmal den Zeitpunkt und Ort unseres Treffens durchgeben, so dass du dich jederzeit bei mir melden kannst, wenn es noch Fragen gibt." +
//					"Wo?: ").append(mOrt).append("" +
//							"Wann?").append(mDatum).append(", ").append(mZeit).append(" Uhr." +
//									"Ich freue mich bereits auf unser Treffen und bis dahin viele Grüße").toString();
//			
//			
//			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mEmail.toString());
//			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, EmailSubject);
//			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, EmailText);
//			
//			startActivity(Intent.createChooser(emailIntent, "Nachricht wird gesendet..."));
//			
//			finish();
//		}
//	};
//	btnEmailSenden.setOnClickListener(oclBtnEmailSenden);

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
	        return true;
	    else
	    	return false;
    }

}
