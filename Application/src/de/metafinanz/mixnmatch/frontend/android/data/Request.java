package de.metafinanz.mixnmatch.frontend.android.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;

public class Request {

	private static final String TAG = "Request";
	
	public static String[] COLUMNS = {Requests.ID, Requests.LOCATION_KEY, Requests.DATE, Requests.USER_ID};
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private String locationKey;
	private Date date;
	private String userid;
	private long id;
	private String _id;
	private String _rev;
	private String url;
	private String matchUrl;
	private String type;
	

	public Request(String locationKey, String reqDate, String userid) {
		super();
		this.locationKey = locationKey;
        
        try {
        	this.date = sdf.parse(reqDate);
		} catch (ParseException e) {
			Log.e(TAG, "Fehler bei Datumskonvertierung.", e);
		}
        
		this.userid = userid;
	}
	
	public Request(Map<String, String> mapData) {
        String type = (String) mapData.get("type");
        String _id = (String) mapData.get("_id");
        String _rev = (String) mapData.get("_rev");
        String reqDate = (String) mapData.get("date");
        String url = (String) mapData.get("url");
        locationKey = (String) mapData.get("locationKey");
        userid = (String) mapData.get("userid");
        
        try {
			date = sdf.parse(reqDate);
		} catch (ParseException e) {
			Log.e(TAG, "Fehler bei Datumskonvertierung.", e);
		}
        
	}
	
	public Request(String locationKey, Date date, String userid) {
		super();
		this.locationKey = locationKey;
		this.date = date;
		this.userid = userid;
	}
	
	public Request() {
	}

	public String getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(String locationKey) {
		this.locationKey = locationKey;
	}

	public String getDateAsString() {
		return sdf.format(date);
	}
	
	public void setDate(String date) {
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			Log.e(TAG, "Fehler beim parsen des Datums", e);
		}
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static final class Requests implements BaseColumns {
		private Requests() {
		}
		
		public static final String type = "requests";

		public static final Uri CONTENT_URI = Uri.parse("content://" + ContProv.AUTHORITY_REQUEST + "/requests");

		public static final String CONTENT_TYPE_QUERY_ITEM = "vnd.android.cursor.item/vnd.mixnmatch.requests";
		public static final String CONTENT_TYPE_QUERY_LIST = "vnd.android.cursor.dir/vnd.mixnmatch.requests";

		public static final String ID = "_id";
		
		public static final String REV = "_rev";
		
		public static final String LOCATION_KEY = "locationKey";

		public static final String DATE = "date";
		
		public static final String USER_ID = "userid";
		
		public static final String TYPE = "type";
		
		public static final String URL = "url";
		
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMatchUrl() {
		return matchUrl;
	}

	public void setMatchUrl(String matchUrl) {
		this.matchUrl = matchUrl;
	}
	
	public Map<String, String> getDataAsMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Requests.LOCATION_KEY, locationKey);
		map.put(Requests.DATE, getDateAsString());
		map.put(Requests.USER_ID, userid);
		
		return map;
	}

	public ContentValues getContentValues() {
		ContentValues contentRequest = new ContentValues();
		contentRequest.put(Requests.LOCATION_KEY, locationKey);
		contentRequest.put(Requests.DATE, getDateAsString());
		contentRequest.put(Requests.USER_ID, userid);
		
		return contentRequest;
	}

	@Override
	public String toString() {
		Log.d(TAG, "toString() von Request.java aufgerufen:" + getContentValues().toString());
		return getContentValues().toString();
	}

	
	
}
