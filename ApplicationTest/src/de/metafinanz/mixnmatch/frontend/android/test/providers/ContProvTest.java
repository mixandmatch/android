package de.metafinanz.mixnmatch.frontend.android.test.providers;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.Location;
import de.metafinanz.mixnmatch.frontend.android.Location.Locations;
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
		super(ContProv.class, ContProv.AUTHORITY);
		Log.d(TAG, "Constructor");
	}
	
	public void testSimpleUri() {
		UriMatcher uriMatcher;
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ContProv.AUTHORITY, ContProv.SUGGEST_URI_PATH_QUERY, 1);
		Uri sampleURI = Uri.parse("content://"+ContProv.AUTHORITY+"/"+ContProv.SUGGEST_URI_PATH_QUERY);
		assertEquals(1, uriMatcher.match(sampleURI));

		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ContProv.AUTHORITY, "/" + ContProv.SUGGEST_URI_PATH_QUERY, 1);
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
		Uri myURI = ContProv.INSERT_URI;
		ContProv provider = getProvider();
		ContentValues values = new ContentValues();
		values.put("key", "HVU");
		values.put("label", "HVU Unterf�hring");
		
		Uri resultUri = provider.insert(myURI, values);
		assertNotNull(resultUri);
		
		
		ContProv providerResult = getProvider();
		Cursor cursorResult = providerResult.query(resultUri, Location.COLUMNS, null, null, null);
		
		assertNotNull(cursorResult);
		
		cursorResult.moveToNext();
        String locName = cursorResult.getString(1);
        assertEquals("HVU Unterf�hring", locName);
	}

	public void testQueryUriStringArrayStringStringArrayString() {
		ContentValues values = new ContentValues();
		values.put("key", "HVU");
		values.put("label", "HVU Unterf�hring");
		ContentLocations.getInstance().insert(values );
		values = new ContentValues();
		values.put("key", "VGU");
		values.put("label", "VGU Unterf�hring");
		ContentLocations.getInstance().insert(values );
		
		Uri myURI = ContProv.CONTENT_ALL_URI;
		ContProv provider = getProvider();
		Cursor cursor = provider.query(myURI, Location.COLUMNS, null, null, null);
		
		assertNotNull(cursor);		
		assertEquals(2, cursor.getCount());
		
		cursor.moveToNext();
		
        String key = cursor.getString(0);
        assertEquals("HVU", key);
        String locName = cursor.getString(1);
        assertEquals("HVU Unterf�hring", locName);

		cursor.moveToNext();

        key = cursor.getString(0);
        assertEquals("VGU", key);
        locName = cursor.getString(1);
        assertEquals("VGU Unterf�hring", locName);
            


	}

//	public void testUpdateUriContentValuesStringStringArray() {
//		fail("Not yet implemented");
//	}

}