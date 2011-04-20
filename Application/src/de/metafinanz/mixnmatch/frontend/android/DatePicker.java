package de.metafinanz.mixnmatch.frontend.android;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DatePicker extends Activity {
	
	private TextView mDateDisplay;
	private Button   mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;

	static final int DATE_DIALOG_ID = 0;
	
	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_date);
        
        //View-Elemente
        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        mPickDate = (Button) findViewById(R.id.pickDate);
        
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
     * Diese Methode updatet das Datum der TextView
     */
    private void updateDisplay() {
    	mDateDisplay.setText(
    			new StringBuilder()
    			.append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
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