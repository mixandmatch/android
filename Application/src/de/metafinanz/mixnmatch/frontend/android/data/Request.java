package de.metafinanz.mixnmatch.frontend.android.data;

import android.net.Uri;
import android.provider.BaseColumns;
import de.metafinanz.mixnmatch.frontend.android.providers.ContProv;

public class Request {
	private String locationKey;
	private String date;
	private String userid;

	public Request(String locationKey, String date, String userid) {
		super();
		this.locationKey = locationKey;
		this.date = date;
		this.userid = userid;
	}

	public String getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(String locationKey) {
		this.locationKey = locationKey;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
