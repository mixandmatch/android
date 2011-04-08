package de.metafinanz.mixnmatch.frontend.android.test.providers;

import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.Location;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;

public class ContProvTest extends ProviderTestCase2<ContProv> {
	private static final String TAG = "ContProvTest";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
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
//	public void testInsertUriContentValues() {
//		fail("Not yet implemented");
//	}

	public void testQueryUriStringArrayStringStringArrayString() {
		Uri myURI = ContProv.CONTENT_URI;
		
		ContProv provider = getProvider();
		
		Cursor cursor = provider.query(myURI, Location.COLUMNS, null, null, null);
		
		assertNotNull(cursor);
		
		assertTrue(cursor.getCount()==0);
//		while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String locName = cursor.getString(1);
//        }


	}

//	public void testUpdateUriContentValuesStringStringArray() {
//		fail("Not yet implemented");
//	}

}
