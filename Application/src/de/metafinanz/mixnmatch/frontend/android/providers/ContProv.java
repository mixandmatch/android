package de.metafinanz.mixnmatch.frontend.android.providers;

import java.util.HashMap;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import de.metafinanz.mixnmatch.frontend.android.Location;
import de.metafinanz.mixnmatch.frontend.android.Location.Locations;

public class ContProv extends ContentProvider {
	private static final String TAG = "ContProv";
	public static final String AUTHORITY = "de.metafinanz.mixnmatch.frontend.android.Location";
    public final static String SUGGEST_URI_PATH_QUERY = "search_all_query";
    
	private static final UriMatcher sURIMatcher;
	private static final String LOCATIONS_TABLE_NAME = "locations";
	private static HashMap<String, String> projectionMap;
	
	private static final int SEARCH_ALL_LOCATIONS = 10;

	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+SUGGEST_URI_PATH_QUERY);

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case SEARCH_ALL_LOCATIONS:
			return Locations.CONTENT_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        if (!TextUtils.isEmpty(selection)) {
            throw new IllegalArgumentException("selection not allowed for " + uri);
        }
        if (selectionArgs != null && selectionArgs.length != 0) {
            throw new IllegalArgumentException("selectionArgs not allowed for " + uri);
        }
        if (!TextUtils.isEmpty(sortOrder)) {
            throw new IllegalArgumentException("sortOrder not allowed for " + uri);
        }
        switch (sURIMatcher.match(uri)) {
            case SEARCH_ALL_LOCATIONS:
                String query = null;
                if (uri.getPathSegments().size() > 1) {
                    query = uri.getLastPathSegment().toLowerCase();
                }
                return getSuggestions(query, projection);
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }

	}

	private Cursor getSuggestions(String query, String[] projection) {
		String processedQuery = query == null ? "" : query.toLowerCase();
		List<Location> locations = ContentLocations.getInstance().getMatches(
				processedQuery);

		MatrixCursor cursor = new MatrixCursor(Location.COLUMNS);
		for (Location loc : locations) {
			cursor.addRow(columnValuesOfLocation(loc));
		}

		return cursor;
	}

	private Object[] columnValuesOfLocation(Location loc) {
		return new Object[] { loc.getId(), // _id
				loc.getLabel() // text1
		};
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	static {
		sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sURIMatcher.addURI(AUTHORITY, SUGGEST_URI_PATH_QUERY, SEARCH_ALL_LOCATIONS);

		projectionMap = new HashMap<String, String>();
		projectionMap.put(Locations.LOCATION_ID, Locations.LOCATION_ID);
		projectionMap.put(Locations.LABLE, Locations.LABLE);

	}

}
