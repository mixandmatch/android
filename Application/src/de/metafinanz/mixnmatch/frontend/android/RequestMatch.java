package de.metafinanz.mixnmatch.frontend.android;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RequestMatch extends AbstractAsyncActivity {
	// um auf die Datumsauswahl umzuleiten
	private Intent iPickDate;
	private Intent iRequestMatch;
	
	private ScrollView mLocations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_match);

		//Feld zum entgegennehmen der Orte
        mLocations = (ScrollView) findViewById(R.id.ortScrollView);
        // TODO hier muss noch der Zugriff aufs Backend und die Befüllung der
		// ScrollView erfolgen!
        
        
		Button btnMatch = (Button) findViewById(R.id.buttonRequestMatch);
		//solange die View zur Ergebnisrückgabe noch nicht fertig ist, leite ich mal lieber auf diese Seite
		//hier um, um Dumps zu vermeiden
		iRequestMatch = new Intent(this, RequestMatch.class);
		
		OnClickListener oclBtnMatches = new OnClickListener() {
			public void onClick(View v) {
				
				// prüfen, ob Name gefüllt ist (wird noch erweitert auf Email usw)
				EditText nameField = (EditText) findViewById(R.id.editName);
				String name = nameField.getText().toString();

				
				if (name.length() == 0) {
					new AlertDialog.Builder(getApplicationContext())
							.setMessage(R.string.error_name_missing)
							.setNeutralButton(R.string.error_ok, null).show();

					return;
				} else {
					
					
					sendMatchWish(new Date(), "me", new Position(0, 0));
					startActivity(iRequestMatch);
				}
			}
		};
		btnMatch.setOnClickListener(oclBtnMatches);

		iPickDate = new Intent(this, DatePicker.class);

		Button btnDate = (Button) findViewById(R.id.buttonPickDate);
		OnClickListener oclBtnDate = new OnClickListener() {
			public void onClick(View v) {
				startActivity(iPickDate);
			}
		};
		btnDate.setOnClickListener(oclBtnDate);

		
	}

	/**
	 * Sendet an den WebService die Daten fuer einen Mittagessenswunsch.
	 * 
	 * @return http return code
	 */
	public void sendMatchWish(Date when, String who, Position position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("day", when.toString());
		parameters.put("who", who);
		parameters.put("where", position);

		AsyncTask<Void, Void, Location[]> task = new RequestLocation(position)
				.execute();
		try {
			task.get();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private class RequestLocation extends AsyncTask<Void, Void, Location[]> {
		protected String TAG = "RequestLocation";

		private Position position;

		public RequestLocation(Position position) {
			super();
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			// before the network request begins, show a progress indicator
			showProgressDialog();
		}

		@Override
		protected Location[] doInBackground(Void... params) {

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// The HttpComponentsClientHttpRequestFactory uses the
			// org.apache.http package to make network requests
			restTemplate
					.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

			// The URL for making the GET request
			// final String url = getString(R.string.base_uri) +
			// "locations/nearby?lat={"+position.getLatitude()+"}&lon={"+position.getLongitude()+"}";
			final String url = getString(R.string.base_uri) + "locations";

			// Initiate the HTTP GET request, expecting an array of State
			// objects in response
			// String result = restTemplate.getForObject(url, String.class);
			// Man kann auch gleich ein POJO statt String erstellen lassen.
			Location[] locations = restTemplate.getForObject(url,
					Location[].class);

			return locations;
		}

		@Override
		protected void onPostExecute(Location[] locations) {
			// hide the progress indicator when the network request is complete
			dismissProgressDialog();

			if (locations != null && locations.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < locations.length; i++) {
					sb.append(locations[i].getLabel());
					sb.append(" (");
					sb.append(locations[i].getKey());
					sb.append(")");
					sb.append("\n");
				}
				Toast toast = Toast.makeText(getApplicationContext(),
						sb.toString(), Toast.LENGTH_LONG);
				toast.show();
			}
		}
	}

}
