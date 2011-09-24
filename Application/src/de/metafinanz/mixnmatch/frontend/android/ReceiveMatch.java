package de.metafinanz.mixnmatch.frontend.android;

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
		cursor = getContentResolver().query(Requests.CONTENT_URI, Request.COLUMNS, null, null, null);
		startManagingCursor(cursor);
		
        awesomeAdapter = new AwesomePagerAdapter();
        awesomePager = (ViewPager) findViewById(R.id.awesomepager);
        awesomePager.setAdapter(awesomeAdapter);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_receive_match, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.refresh:
	        refreshRequests();
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

	private class AwesomePagerAdapter extends PagerAdapter{
		
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
			Log.d(TAG, "Instatiate view for position " + position);
			Request request = getData(position);
			
			
			LinearLayout parent = new LinearLayout(context);
			LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tvDate = new TextView(context);
			tvDate.setText("Datum: " + request.getDate());
			tvDate.setTextColor(Color.WHITE);
			tvDate.setTextSize(30);

			TextView tvLocation = new TextView(context);
			tvLocation.setText("Lokation: " + request.getLocationKey());
			tvLocation.setTextColor(Color.WHITE);
			tvLocation.setTextSize(30);

			TextView tvCompanions = new TextView(context);
			tvCompanions.setText("Mitesser: -" );
			tvCompanions.setTextColor(Color.WHITE);
			tvCompanions.setTextSize(20);
			
			parent.addView(tvDate, params);
			parent.addView(tvLocation, params);
			parent.addView(tvCompanions, params);

			
			((ViewPager) collection).addView(parent,0);
			
			TextView tv = new TextView(context);
            tv.setText("Bonjour PAUG " + position);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(30);
            
            ((ViewPager) collection).addView(tv,0);
            return tv;
			
//			return parent;
		}
		
		private Request getData(int position) {
			initCursor();
			cursor.moveToFirst();
			cursor.move(position);
			Request request = new Request();
			String id = cursor.getString(cursor.getColumnIndex(Requests.ID));
			String locKey = cursor.getString(cursor.getColumnIndex(Requests.LOCATION_KEY));
			String tmpdate = cursor.getString(cursor.getColumnIndex(Requests.DATE));
			Date date = new Date(tmpdate);
			String userId = cursor.getString(cursor.getColumnIndex(Requests.USER_ID));
			request.setId(Integer.parseInt(id));
			request.setLocationKey(locKey);
			request.setDate(date);
			request.setUserid(userId);
			
			return request;
		}

		private void initCursor() {
			if (cursor == null) {
				cursor = getContentResolver().query(Requests.CONTENT_URI, Request.COLUMNS, null, null, null);
				startManagingCursor(cursor);
			}
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
//			((ViewPager) collection).removeView((LinearLayout) view);
			((ViewPager) collection).removeView((TextView) view);
		}

		
		@Override
		public boolean isViewFromObject(View view, Object object) {
//			return view==((LinearLayout)object);
			return view==((TextView)object);
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
    	
    }
}
