package de.metafinanz.mixnmatch.frontend.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReceiveMatch extends AbstractAsyncActivity {
	private TextView mName;
	private TextView mEmail;
	private TextView mOrt;
	private TextView mDatum;
	private TextView mZeit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_match);

		mName  = (TextView) findViewById(R.id.AnzeigePartnerName);
		mEmail = (TextView) findViewById(R.id.AnzeigePartnerEmail);
		mOrt   = (TextView) findViewById(R.id.AnzeigePartnerOrt);
		mDatum = (TextView) findViewById(R.id.AnzeigeDatumTreff);
		mZeit  = (TextView) findViewById(R.id.AnzeigeZeitTreff);
		
		mName.setText("Hans Wurst");
		mEmail.setText("hans.wurst@metzger.de");
		mOrt.setText("Metzgerei Dimpflmoser");
		mDatum.setText("02.05.2011");
		mZeit.setText("12:00");
		
		Button btnEmailSenden = (Button) findViewById(R.id.buttonEmailSenden);
        OnClickListener oclBtnEmailSenden = new OnClickListener() {
			public void onClick(View v) {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				
				String EmailSubject = new String();
				String EmailText = new String();
				
				EmailSubject = new StringBuilder().append("Unser Mix and Match Treffen am ").append(mDatum).toString();
				
				EmailText = new StringBuilder().append("Lieber ").append(mName).append(", " +
						"wir wurden so eben ausgelost für ein gemeinsames Mittagessen." +
						"Deswegen wollte ich dir nur nochmal den Zeitpunkt und Ort unseres Treffens durchgeben, so dass du dich jederzeit bei mir melden kannst, wenn es noch Fragen gibt." +
						"Wo?: ").append(mOrt).append("" +
								"Wann?").append(mDatum).append(", ").append(mZeit).append(" Uhr." +
										"Ich freue mich bereits auf unser Treffen und bis dahin viele Grüße").toString();
				
				
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mEmail.toString());
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, EmailSubject);
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, EmailText);
				
				startActivity(Intent.createChooser(emailIntent, "Nachricht wird gesendet..."));
				
				finish();
			}
		};
		btnEmailSenden.setOnClickListener(oclBtnEmailSenden);
		
	}

}
