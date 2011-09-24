package de.metafinanz.mixnmatch.frontend.android.test.providers;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.data.Request;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;
import de.metafinanz.mixnmatch.frontend.android.providers.ContentLocations;

public class ContProvTest extends ProviderTestCase2<ContProv> {
	private static final String TAG = "ContProvTest";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ContentLocations.getInstance().removeAll();
        Log.d(TAG, "setUp");
    }


	public ContProvTest() {
		super(ContProv.class, ContProv.AUTHORITY_LOCATION);
		Log.d(TAG, "Constructor");
	}
	
	public void testSimpleUri() {
		UriMatcher uriMatcher;
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ContProv.AUTHORITY_LOCATION, ContProv.SUGGEST_URI_PATH_QUERY, 1);
		Uri sampleURI = Uri.parse("content://"+ContProv.AUTHORITY_LOCATION+"/"+ContProv.SUGGEST_URI_PATH_QUERY);
		assertEquals(1, uriMatcher.match(sampleURI));

		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ContProv.AUTHORITY_LOCATION, "/" + ContProv.SUGGEST_URI_PATH_QUERY, 1);
		assertEquals(UriMatcher.NO_MATCH, uriMatcher.match(sampleURI));

	}

	public void testOnCreate() {
    	Log.d(TAG, "testOnCreate");
		ContProv provider = new ContProv();
		assertNotNull(provider);
		
		assertFalse(provider.onCreate());
	}

//	public void testDeleteUriStringStringArray() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetTypeUri() {
//		fail("Not yet implemented");
//	}
//
	public void testInsertUriContentValues() {
		ContProv provider = getProvider();
		ContentValues values = new ContentValues();
		values.put(Locations.KEY, "HVU");
		values.put(Locations.LABLE, "HVU Unterföhring");
		
		Uri resultUri = provider.insert(Locations.CONTENT_URI, values);
		assertNotNull(resultUri);

		Log.d(TAG, "Insert-URI: " + Locations.CONTENT_URI);
		Log.d(TAG, "Result-URI: " + resultUri);
		
		
		ContProv providerResult = getProvider();
		Cursor cursorResult = providerResult.query(resultUri, Location.COLUMNS, null, null, null);
		
		assertNotNull(cursorResult);
		
		cursorResult.moveToNext();
        String locName = cursorResult.getString(2);
        assertEquals("HVU Unterföhring", locName);
	}

	public void testQueryUriStringArrayStringStringArrayString() {
		ContentValues values = new ContentValues();
		values.put(Locations.KEY, "HVU");
		values.put(Locations.LABLE, "HVU Unterföhring");
		ContentLocations.getInstance().insert(values );
		values = new ContentValues();
		values.put(Locations.KEY, "VGU");
		values.put(Locations.LABLE, "VGU Unterföhring");
		ContentLocations.getInstance().insert(values );
		
		ContProv provider = getProvider();
		Cursor cursor = provider.query(Locations.CONTENT_URI, Location.COLUMNS, null, null, null);
		
		assertNotNull(cursor);		
		assertEquals(2, cursor.getCount());
		
		cursor.moveToNext();
		
        String key = cursor.getString(1);
        assertEquals("HVU", key);
        String locName = cursor.getString(2);
        assertEquals("HVU Unterföhring", locName);

		cursor.moveToNext();

        key = cursor.getString(1);
        assertEquals("VGU", key);
        locName = cursor.getString(2);
        assertEquals("VGU Unterföhring", locName);
            


	}
	
	public void testInsertRequestUriContentValues() {
		ContProv provider = getProvider();
		ContentValues values = new ContentValues();
		values.put(Requests.LOCATION_KEY, "HVU");
		values.put(Requests.DATE, "03.08.2011");
		values.put(Requests.USER_ID, "TSP");
		
		Uri resultUri = provider.insert(Requests.CONTENT_URI, values);
		assertNotNull(resultUri);

		Log.d(TAG, "Insert-URI: " + Requests.CONTENT_URI);
		Log.d(TAG, "Result-URI: " + resultUri);
		
		
		ContProv providerResult = getProvider();
		Cursor cursorResult = providerResult.query(resultUri, Request.COLUMNS, null, null, null);
		
		assertNotNull(cursorResult);
		
		cursorResult.moveToNext();
        String locKey = cursorResult.getString(0);
        assertEquals("HVU", locKey);
        String date = cursorResult.getString(1);
        assertEquals("2011.08.03", date);
        String userId = cursorResult.getString(2);
        assertEquals("TSP", userId);
	}

//	public void testUpdateUriContentValuesStringStringArray() {
//		fail("Not yet implemented");
//	}

}
