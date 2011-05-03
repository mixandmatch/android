package de.metafinanz.mixnmatch.frontend.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.Location.Locations;

public class RequestMatch extends AbstractAsyncActivity {
	private static final String TAG = "RequestMatch";
	
	private Intent iMixAndMatch;
	private Intent iLocationDialog;
	private Button   mPickDate;
	private Button   mPickTime;
	private Calendar meetingCalendar = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_match);
        

		prepareLocationsSpinner();
		
//		
//		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		iLocationDialog = new Intent(this, LocationDialog.class);
        
//        Button btnLocationView = (Button) findViewById(R.id.buttonLocationView);
//        OnClickListener oclBtnLocationView = new OnClickListener() {
//			public void onClick(View v) {
//				startActivity(iLocationDialog);
//			}
//		};
//		btnLocationView.setOnClickListener(oclBtnLocationView);
		
		//Datums-Button
		TextView mDateDisplay = (TextView) findViewById(R.id.textDatumWert);
        mPickDate = (Button) findViewById(R.id.buttonPickDate);
        
        //ClickListener für den Button
        mPickDate.setOnClickListener(new View.OnClickListener() {
    	   public void onClick(View v) {
    		   showDialog(DATE_DIALOG_ID);
    	   }
        });
        
//        //Zeit-Button
//        mTimeDisplay = (TextView) findViewById(R.id.TextUhrzeitWert);
//        mPickTime = (Button) findViewById(R.id.buttonPickTime);
//        
//        //ClickListener für den Button
//        mPickTime.setOnClickListener(new View.OnClickListener() {
//    	   public void onClick(View v) {
//    		   showDialog(TIME_DIALOG_ID);
//    	   }
//        });
        
        init();
       
        //aktuelles Datum anzeigen
        updateDisplay();
        
		//solange die View zur Ergebnisrückgabe noch nicht fertig ist, leite ich mal lieber auf diese Seite
		//hier um, um Dumps zu vermeiden
		iMixAndMatch = new Intent(this, MixAndMatch.class);
		
		Button btnMatchSenden = (Button) findViewById(R.id.buttonMatchwunschSenden);
		OnClickListener oclBtnMatchesSenden = new OnClickListener() {
			public void onClick(View v) {
				
				// prüfen, ob Name gefüllt ist (wird noch erweitert auf Email usw)
				EditText mEditName = (EditText) findViewById(R.id.editName);
				String name = mEditName.getText().toString();
				EditText mEditEmail = (EditText) findViewById(R.id.editEMail);
				String mail = mEditEmail.getText().toString();
				TextView mDateDisplay = (TextView) findViewById(R.id.textDatumWert);
				String date = mDateDisplay.getText().toString();
				
//				if (name.length() == 0) {
//					new AlertDialog.Builder(getApplicationContext())
//							.setMessage(R.string.error_name_missing)
//							.setNeutralButton(R.string.error_ok, null).show();
//
//					return;
//				} 
//				else if  (mail.length() == 0) {
//					new AlertDialog.Builder(getApplicationContext())
//					.setMessage(R.string.error_mail_missing)
//					.setNeutralButton(R.string.error_ok, null).show();
//					return;
//				}
//				else {	
//					sendMatchWish(mDateDisplay.getText().toString(), mEditName.getText().toString(), new Position(0, 0));
//					startActivity(iMixAndMatch);
//				}

				Toast toast = Toast.makeText(getApplicationContext(), "Matchwunsch wird gesendet." , Toast.LENGTH_LONG);
				toast.show();
				sendMatchWish(mDateDisplay.getText().toString(), name, new Position(0, 0));
				startActivity(iMixAndMatch);
			}
		};
		btnMatchSenden.setOnClickListener(oclBtnMatchesSenden);

		
	}

	private void prepareLocationsSpinner() {
		//Ort auswählen
		Spinner spinner = (Spinner) findViewById(R.id.OrtSpinner);
		Cursor cursor = getContentResolver().query(Locations.CONTENT_URI, Location.COLUMNS, null, null, null);
		startManagingCursor(cursor);
		
		Log.d(TAG, "Anzahl im Cursor: " + cursor.getCount());
		
		String[] from = new String[] { Locations.LABLE } ;
		int[] to = new int[] { android.R.id.text1 };
		
		SimpleCursorAdapter locationsCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, from, to);
		locationsCursorAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spinner.setAdapter(locationsCursorAdapter);
	}

	private void init() {
		meetingCalendar = Calendar.getInstance();
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

		//Aufruf des Service, welche die Daten in einer Queue ablegt und sendet, sobald ein Signal vorhanden ist.
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
		TextView tvDateDisplay = (TextView) findViewById(R.id.textDatumWert);
		tvDateDisplay.setText(sdf.format(meetingCalendar.getTime()));
    }
	
    //Rückgabe des ausgewählten Datums
    private DatePickerDialog.OnDateSetListener mDateSetListener =
    		new DatePickerDialog.OnDateSetListener() {

				public void onDateSet(android.widget.DatePicker view, int year,
								   	  int monthOfYear, int dayOfMonth) {
					
					meetingCalendar.set(Calendar.YEAR, year);
					meetingCalendar.set(Calendar.MONTH, monthOfYear);
					meetingCalendar.set(Calendar.DATE, dayOfMonth);
					updateDisplay();
			
		}
	};
	
    //Rückgabe der ausgewählten Zeit
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
    		new TimePickerDialog.OnTimeSetListener() {

				public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
								   	  int minute) {
					meetingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					meetingCalendar.set(Calendar.MINUTE, minute);
					updateDisplay();
			
		}
	};
		
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DATE_DIALOG_ID) {
			return new DatePickerDialog(this, mDateSetListener, 
					meetingCalendar.get(Calendar.YEAR), 
					meetingCalendar.get(Calendar.MONTH), 
					meetingCalendar.get(Calendar.DATE));
		} 
		else if (id == TIME_DIALOG_ID) {
			return new TimePickerDialog(this, mTimeSetListener, 
					meetingCalendar.get(Calendar.HOUR_OF_DAY), 
					meetingCalendar.get(Calendar.MINUTE), false);
		} 
		else {
			return null;
		}
	}


}
