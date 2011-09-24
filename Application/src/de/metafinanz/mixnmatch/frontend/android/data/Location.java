package de.metafinanz.mixnmatch.frontend.android.data;

import android.net.Uri;
import android.provider.BaseColumns;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;

public class Location {
	
	public static String[] COLUMNS = {Locations.LOCATION_ID, Locations.KEY, Locations.LABLE};

	private long id;
	private String key;
	private String label;
	private Position coordinates;
	private String description;
	private String venue;
	

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

	public Location() {
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Position getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Position coordinates) {
		this.coordinates = coordinates;
	}


	@Override
	public String toString() {
		return key;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}


	public static final class Locations implements BaseColumns {
		private Locations() {
		}
		
		public static final String type = "locations";

		public static final Uri CONTENT_URI = Uri.parse("content://" + ContProv.AUTHORITY_LOCATION + "/locations");

		public static final String CONTENT_TYPE_QUERY_ITEM = "vnd.android.cursor.item/vnd.mixnmatch.locations";
		public static final String CONTENT_TYPE_QUERY_LIST = "vnd.android.cursor.dir/vnd.mixnmatch.locations";

		public static final String LOCATION_ID = "_id";
		
		public static final String KEY = "key";

		public static final String LABLE = "lable";
		
		public static final String COORDINATE_LONGITUDE = "lon";
		
		public static final String COORDINATE_LADITUDE = "lat";
		
		public static final String DESCRIPTION = "description";
		
		public static final String VENUE = "venue";
	}
}
