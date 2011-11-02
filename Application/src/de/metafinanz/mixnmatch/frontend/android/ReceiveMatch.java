package de.metafinanz.mixnmatch.frontend.android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;
import com.jakewharton.android.viewpagerindicator.TitleProvider;

import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.data.Request;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;
import de.metafinanz.mixnmatch.frontend.android.services.DataServiceHelper;


public class ReceiveMatch extends Activity {
	private static final String TAG = "ReceiveMatch";
	
	private Cursor cursor;
	private Context context;
	
	private ViewPager awesomePager;
	private AwesomePagerAdapter awesomeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_match);
		this.context = this;
		
		initCursor();
		
        awesomeAdapter = new AwesomePagerAdapter();
        awesomePager = (ViewPager) findViewById(R.id.awesomepager);
        TitlePageIndicator indicator =
                (TitlePageIndicator)findViewById( R.id.indicator );
        awesomePager.setAdapter(awesomeAdapter);
        indicator.setViewPager( awesomePager );
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_receive_match, menu);
	    return true;
	}
	
	/**
	 * Menü-Aktionen auswerten
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.refresh:
	        refreshRequests();
	        return true;
	    case R.id.delete:
	        deleteRequest();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}


	private void refreshRequests() {
		DataServiceHelper.getInstance(this).getRequests(true);
		cursor = getContentResolver().query(Requests.CONTENT_URI, Request.COLUMNS, null, null, null);
		startManagingCursor(cursor);
	}
	
	private void deleteRequest() {
		DataServiceHelper.getInstance(this).deleteRequest(awesomeAdapter.getActualDate(), awesomeAdapter.getActualLocationKey());
		cursor = getContentResolver().query(Requests.CONTENT_URI, Request.COLUMNS, null, null, null);
		startManagingCursor(cursor);
	}


	private void initCursor() {
		if (cursor == null) {
			cursor = getContentResolver().query(Requests.CONTENT_URI, Request.COLUMNS, null, null, null);
			startManagingCursor(cursor);
		}
	}

	private class AwesomePagerAdapter extends PagerAdapter implements TitleProvider{
		
		private String actualLocationKey;
		private Date actualDate;
		
		@Override
		public int getCount() {
			initCursor();
			Log.d(TAG, "Received " + cursor.getCount() + " item(s)");
			return cursor.getCount();
		}

	    /**
	     * Create the page for the given position.  The adapter is responsible
	     * for adding the view to the container given here, although it only
	     * must ensure this is done by the time it returns from
	     * {@link #finishUpdate()}.
	     *
	     * @param container The containing View in which the page will be shown.
	     * @param position The page position to be instantiated.
	     * @return Returns an Object representing the new page.  This does not
	     * need to be a View, but can be some other container of the page.
	     */
		@Override
		public Object instantiateItem(View collection, int position) {
			Log.d(TAG, "Instantiate view for position " + position);
			Request request = getData(position);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			
			LinearLayout parent = new LinearLayout(context);
			parent.setOrientation(LinearLayout.VERTICAL); 
			parent.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tvDate = new TextView(context);
			tvDate.setText("Datum: " + sdf.format(request.getDate()));
			tvDate.setTextColor(Color.BLACK);
			tvDate.setTextSize(15);
			tvDate.setLayoutParams(params);

			TextView tvLocation = new TextView(context);
			tvLocation.setText("Lokation: " + request.getLocationKey());
			tvLocation.setTextColor(Color.BLACK);
			tvLocation.setTextSize(15);
			tvLocation.setLayoutParams(params);

			TextView tvLocationDescription = new TextView(context);
			tvLocationDescription.setText("Beschreibung: " + getLocationDescription(request.getLocationKey()));
			tvLocationDescription.setTextColor(Color.BLACK);
			tvLocationDescription.setTextSize(15);
			tvLocationDescription.setLayoutParams(params);

			TextView tvCompanions = new TextView(context);
			tvCompanions.setText("Mitesser: -" );
			tvCompanions.setTextColor(Color.BLACK);
			tvCompanions.setTextSize(15);
			tvCompanions.setLayoutParams(params);
			
			parent.addView(tvDate);
			parent.addView(tvLocation);
			parent.addView(tvLocationDescription);
			parent.addView(tvCompanions);
			
			
			((ViewPager) collection).addView(parent, 0);
			
			return parent;

//			TextView tv = new TextView(context);
//            tv.setText("Bonjour PAUG " + position);
//            tv.setTextColor(Color.BLACK);
//            tv.setTextSize(30);
//            
//            ((ViewPager) collection).addView(tv,0);
            
			
		}
		
		private String getLocationDescription(String locationKey) {

			Cursor locCursor = getContentResolver().query(Locations.CONTENT_URI_LOCATION_ITEM, Location.COLUMNS_DESCRIPTION, locationKey, null, null);
			if (locCursor != null) {
				locCursor.moveToFirst();

				String description = locCursor.getString(locCursor.getColumnIndex(Locations.DESCRIPTION));
				return description;
			}
			return null;
		}

		private Request getData(int position) {
			initCursor();
			cursor.moveToFirst();
			cursor.move(position);
			Request request = new Request();
			String id = cursor.getString(cursor.getColumnIndex(Requests.ID));
			String actualLocationKey = cursor.getString(cursor.getColumnIndex(Requests.LOCATION_KEY));
			String tmpdate = cursor.getString(cursor.getColumnIndex(Requests.DATE));
			actualDate = null;
			try {
				actualDate = DateFormat.getInstance().parse(tmpdate);
			} catch (ParseException e) {
				Log.e(TAG, "Fehler beim parsen des Datums ("+tmpdate+"). Verwende 1.1.1970.");
				actualDate = new Date(0);
			}
			String userId = cursor.getString(cursor.getColumnIndex(Requests.USER_ID));
			request.setId(Integer.parseInt(id));
			request.setLocationKey(actualLocationKey);
			request.setDate(actualDate);
			request.setUserid(userId);
			
			return request;
		}

	    /**
	     * Remove a page for the given position.  The adapter is responsible
	     * for removing the view from its container, although it only must ensure
	     * this is done by the time it returns from {@link #finishUpdate()}.
	     *
	     * @param container The containing View from which the page will be removed.
	     * @param position The page position to be removed.
	     * @param object The same object that was returned by
	     * {@link #instantiateItem(View, int)}.
	     */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((LinearLayout) view);
//			((ViewPager) collection).removeView((TextView) view);
		}

		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==((LinearLayout)object);
//			return view==((TextView)object);
		}
		
	    /**
	     * Called when the a change in the shown pages has been completed.  At this
	     * point you must ensure that all of the pages have actually been added or
	     * removed from the container as appropriate.
	     * @param container The containing View which is displaying this adapter's
	     * page views.
	     */
		@Override
		public void finishUpdate(View arg0) {}
		

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {}

		@Override
		public String getTitle(int position) {

			Request request = getData(position);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			
			return sdf.format(request.getDate());
		}

		public String getActualLocationKey() {
			return actualLocationKey;
		}

		public Date getActualDate() {
			return actualDate;
		}
    	
    }
}
