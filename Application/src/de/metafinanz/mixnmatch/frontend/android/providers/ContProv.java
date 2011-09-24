package de.metafinanz.mixnmatch.frontend.android.providers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;
import de.metafinanz.mixnmatch.frontend.android.data.Request;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;

public class ContProv extends ContentProvider {
	private static final String TAG = "ContProv";
	public static final String AUTHORITY_LOCATION = "de.metafinanz.mixnmatch.frontend.android.data.Location";
	public static final String AUTHORITY_REQUEST = "de.metafinanz.mixnmatch.frontend.android.data.Request";
	public final static String SUGGEST_URI_PATH_QUERY_ALL = "search_all_query";
	public final static String SUGGEST_URI_PATH_QUERY = "search_query";

	private static final UriMatcher sURIMatcher;
//	private static HashMap<String, String> projectionMap;

	private static final int SEARCH_ALL_LOCATIONS = 10;
	private static final int SEARCH_ONE_LOCATIONS = 15;
	private static final int SEARCH_ALL_REQUESTS = 20;
	

	public static final Uri CONTENT_ALL_URI = Uri.parse("content://" + AUTHORITY_LOCATION + "/" + SUGGEST_URI_PATH_QUERY_ALL);
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_LOCATION + "/" + SUGGEST_URI_PATH_QUERY);
	public static final Uri CONTENT_REQUESTS_ALL_URI = Uri.parse("content://" + AUTHORITY_REQUEST + "/" + SUGGEST_URI_PATH_QUERY);

	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case SEARCH_ALL_LOCATIONS:
			return Locations.CONTENT_TYPE_QUERY_LIST;

		case SEARCH_ONE_LOCATIONS:
			return Locations.CONTENT_TYPE_QUERY_ITEM;
			
		case SEARCH_ALL_REQUESTS:
			return Requests.CONTENT_TYPE_QUERY_LIST;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * Not supported
	 */
	public Uri insert(Uri uri, ContentValues values) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");

		switch (sURIMatcher.match(uri)) {
		case SEARCH_ALL_LOCATIONS:
			Long id = ContentLocations.getInstance().insert(values);

			if (id != null) {
				Uri _uri = ContentUris.withAppendedId(Locations.CONTENT_URI, id);
				getContext().getContentResolver().notifyChange(_uri, null);
				return _uri;
			}
			return null;
		case SEARCH_ALL_REQUESTS:
			Date date = ContentRequests.getInstance().insert(values);

			if (date != null) {
				Uri _uri = ContentUris.withAppendedId(Requests.CONTENT_URI, Integer.valueOf(sdf.format(date)));
				getContext().getContentResolver().notifyChange(_uri, null);
				return _uri;
			}
			return null;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

	}

	@Override
	/**
	 * Not supported
	 */
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		ContentLocations.getInstance().removeAll();
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query qith uri " + uri.toString());
		if (!TextUtils.isEmpty(selection)) {
			throw new IllegalArgumentException("selection not allowed for "
					+ uri);
		}
		if (selectionArgs != null && selectionArgs.length != 0) {
			throw new IllegalArgumentException("selectionArgs not allowed for "
					+ uri);
		}
		if (!TextUtils.isEmpty(sortOrder)) {
			throw new IllegalArgumentException("sortOrder not allowed for "
					+ uri);
		}
		String query = null;
		switch (sURIMatcher.match(uri)) {
		case SEARCH_ALL_LOCATIONS:
			if (uri.getPathSegments().size() > 1) {
				query = uri.getLastPathSegment().toLowerCase();
			}
			return getSuggestionsLocations(query, projection);
		case SEARCH_ONE_LOCATIONS:
			Long index = Long.valueOf(uri.getLastPathSegment());
			return getOneLocation(index, projection);

		case SEARCH_ALL_REQUESTS:
			if (uri.getPathSegments().size() > 1) {
				query = uri.getLastPathSegment().toLowerCase();
			}
			return getSuggestionsRequests(query, projection);
			
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

	}

	private Cursor getOneLocation(Long index, String[] projection) {
		ContentLocations instanceOfContentLocations = ContentLocations.getInstance();
		Location location = instanceOfContentLocations.getMatch(index);

		if (location != null) {
			Log.i(TAG, "found location" + location.getKey());
			MatrixCursor cursor = new MatrixCursor(Location.COLUMNS);
			cursor.addRow(columnValuesOfLocation(location));
	
			return cursor;
		}
		return null;
	}

	private Cursor getSuggestionsLocations(String query, String[] projection) {
		String processedQuery = query == null ? "" : query.toLowerCase();
		List<Location> locations = ContentLocations.getInstance().getMatches(
				processedQuery);

		Log.i(TAG, "found " + locations.size() + " results");
		MatrixCursor cursor = new MatrixCursor(Location.COLUMNS);
		int counter = 0;
		for (Location loc : locations) {
			loc.setId(counter++);
			cursor.addRow(columnValuesOfLocation(loc));
		}

		return cursor;
	}	
	private Cursor getSuggestionsRequests(String query, String[] projection) {
		String processedQuery = query == null ? "" : query.toLowerCase();
		List<Request> requests = ContentRequests.getInstance().getMatches(
				processedQuery);

		Log.i(TAG, "found " + requests.size() + " results");
		MatrixCursor cursor = new MatrixCursor(Request.COLUMNS);
		int counter = 0;
		for (Request req: requests) {
			req.setId(counter++);
			cursor.addRow(columnValuesOfRequest(req));
		}

		return cursor;
	}

	private Object[] columnValuesOfLocation(Location loc) {
		return new Object[] { loc.getId(), // id
				loc.getKey(), // key
				loc.getLabel() // lable
		};
	}
	
	private Object[] columnValuesOfRequest(Request req) {
		return new Object[] { req.getId(), // id
				req.getLocationKey(), // location key
				req.getDate().toString(), // datum
				req.getUserid() // user key
				 
		};
	}

	@Override
	/**
	 * Not supported
	 */
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

	static {
		sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sURIMatcher.addURI(AUTHORITY_LOCATION, Locations.type, SEARCH_ALL_LOCATIONS);
		sURIMatcher.addURI(AUTHORITY_LOCATION, Locations.type + "/#", SEARCH_ONE_LOCATIONS);
		sURIMatcher.addURI(AUTHORITY_REQUEST, Requests.type, SEARCH_ALL_REQUESTS);
		
		

//		projectionMap = new HashMap<String, String>();
//		projectionMap.put(Locations.LOCATION_ID, Locations.LOCATION_ID);
//		projectionMap.put(Locations.KEY, Locations.KEY);
//		projectionMap.put(Locations.LABLE, Locations.LABLE);

	}

}
