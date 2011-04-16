package de.metafinanz.mixnmatch.frontend.android;

import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;
import android.net.Uri;
import android.provider.BaseColumns;

public class Location {
	
	public static String[] COLUMNS = {Locations.LOCATION_ID, Locations.LABLE};
	
	private int id;
	private String key;
	private String label;
	

	public Location(int id, String label) {
		super();
		this.id = id;
		this.label = label;
	}
	public Location(String key, String label) {
		super();
		this.key = key;
		this.label = label;
	}

	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public Location() {
	}

	public static final class Locations implements BaseColumns {
		private Locations() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ ContProv.AUTHORITY + "/locations");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.locations";

		public static final String LOCATION_ID = "_id";
		
		public static final String KEY = "key";

		public static final String LABLE = "lable";
	}
}
