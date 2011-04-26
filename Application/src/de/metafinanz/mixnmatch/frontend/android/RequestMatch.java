package de.metafinanz.mixnmatch.frontend.android;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RequestMatch extends AbstractAsyncActivity {
	private Intent iRequestMatch;
	private EditText mName;
	private TextView mDateDisplay;
	private Button   mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_match);
        
        
		Button btnMatch = (Button) findViewById(R.id.buttonRequestMatch);
		//solange die View zur Ergebnisrückgabe noch nicht fertig ist, leite ich mal lieber auf diese Seite
		//hier um, um Dumps zu vermeiden
		iRequestMatch = new Intent(this, RequestMatch.class);
		
		OnClickListener oclBtnMatches = new OnClickListener() {
			public void onClick(View v) {
				
				// prüfen, ob Name gefüllt ist (wird noch erweitert auf Email usw)
				mName = (EditText) findViewById(R.id.editName);
				String name = mName.getText().toString();

				
				if (name.length() == 0) {
					new AlertDialog.Builder(getApplicationContext())
							.setMessage(R.string.error_name_missing)
							.setNeutralButton(R.string.error_ok, null).show();

					return;
				} else {
					
					
					sendMatchWish(mDateDisplay.getText().toString(), mName.getText().toString(), new Position(0, 0));
					startActivity(iRequestMatch);
				}
			}
		};
		btnMatch.setOnClickListener(oclBtnMatches);

		//Ort auswählen
		Spinner spinner = (Spinner) findViewById(R.id.ortSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		//Datums-Button
		mDateDisplay = (TextView) findViewById(R.id.TextDatumWert);
        mPickDate = (Button) findViewById(R.id.buttonPickDate);
        
        //ClickListener für den Button
        mPickDate.setOnClickListener(new View.OnClickListener() {
    	   public void onClick(View v) {
    		   showDialog(DATE_DIALOG_ID);
		}
       });
       
       //aktuelles Datum beschaffen
       final Calendar c = Calendar.getInstance();
       mYear = c.get(Calendar.YEAR);
       mMonth = c.get(Calendar.MONTH);
       mDay = c.get(Calendar.DAY_OF_MONTH);
       
       //aktuelles Datum anzeigen
       updateDisplay();

		
	}

	/**
	 * Sendet an den WebService die Daten fuer einen Mittagessenswunsch.
	 * 
	 * @return http return code
	 */
	public void sendMatchWish(String when, String who, Position position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("day", when);
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
	private void updateDisplay() {
    	mDateDisplay.setText(
    			new StringBuilder()
    			.append(mDay).append(".")
    			.append(mMonth + 1).append(".")
    			.append(mYear).append(" "));
    }
    
    //Rückgabe des ausgewählten Datums
    private DatePickerDialog.OnDateSetListener mDateSetListener =
    		new DatePickerDialog.OnDateSetListener() {

				public void onDateSet(android.widget.DatePicker view, int year,
								   	  int monthOfYear, int dayOfMonth) {
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					updateDisplay();
			
		}
		};
		
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

}
