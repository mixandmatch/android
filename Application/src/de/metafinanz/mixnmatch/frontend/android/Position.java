package de.metafinanz.mixnmatch.frontend.android;

public class Position {
	
	private double lat;
	private double lon;
	
	public Position() {	
		super();
	}
	
	public Position(double latitude, double longitude) {
		super();
		this.lat = latitude;
		this.lon = longitude;
	}
	
	public Position(String latitude, String longitude) {
		super();
		this.lat = Double.valueOf(latitude);
		this.lon = Double.valueOf(longitude);
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	

	
}
