package de.metafinanz.mixnmatch.frontend.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.services.DataServiceHelper;

public class RequestMatch extends AbstractAsyncActivity {
	private static final String TAG = "RequestMatch";
	
	private Intent iMixAndMatch;
	private Button   mPickDate;
	private Calendar meetingCalendar = null;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	
	private String selectedPlace = "";
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_match);

		prepareLocationsSpinner();
		
		//Datums-Button
        mPickDate = (Button) findViewById(R.id.buttonPickDate);
        
        //ClickListener für den Button
        mPickDate.setOnClickListener(new View.OnClickListener() {
    	   public void onClick(View v) {
    		   showDialog(DATE_DIALOG_ID);
    	   }
        });
        
        init();
       
        //aktuelles Datum anzeigen
        updateDisplay();
        
		//solange die View zur Ergebnisrückgabe noch nicht fertig ist, leite ich mal lieber auf diese Seite
		//hier um, um Dumps zu vermeiden
		iMixAndMatch = new Intent(this, MixAndMatch.class);
		
		Button btnMatchSenden = (Button) findViewById(R.id.buttonMatchwunschSenden);
		OnClickListener oclBtnMatchesSenden = new OnClickListener() {
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Matchwunsch wird gesendet.", Toast.LENGTH_LONG);
				toast.show();
				sendMatchWish();
				startActivity(iMixAndMatch); //Zurück auf die Startseite
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
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				Cursor c = (Cursor)parent.getItemAtPosition(position);
				selectedPlace = c.getString(c.getColumnIndexOrThrow(Location.Locations.KEY));
				
				Toast.makeText( parent.getContext(),
						"Gewählter Ort: " + selectedPlace, Toast.LENGTH_LONG).show();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing.
			}
		});
		
	}
	

	private void init() {
		meetingCalendar = Calendar.getInstance();
		meetingCalendar.set(Calendar.HOUR, 0);
		meetingCalendar.set(Calendar.MINUTE, 0);
	}

	/**
	 * Sendet an den WebService die Daten fuer einen Mittagessenswunsch.
	 * 
	 * @return http return code
	 */
	public void sendMatchWish() {

		Date time = meetingCalendar.getTime();
		DataServiceHelper.getInstance(this).postRequest(selectedPlace , time);

	}


	private void updateDisplay() {
		TextView tvDateDisplay = (TextView) findViewById(R.id.TextDatumWert);
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
					Toast.makeText( view.getContext(),
							"Gewählte Zeit: " + sdf.format(meetingCalendar.getTime()), Toast.LENGTH_LONG).show();
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
