package de.metafinanz.mixnmatch.frontend.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import de.metafinanz.mixnmatch.frontend.android.data.Request;

public class MMApplication extends Application {
	
	private List<Request> requests = new ArrayList<Request>();
	private String userID = "tsp";

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public String getUserId() {
		return userID ;
	}
	
	

}
