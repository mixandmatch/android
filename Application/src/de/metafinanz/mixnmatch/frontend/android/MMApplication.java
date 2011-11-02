package de.metafinanz.mixnmatch.frontend.android;

import java.text.SimpleDateFormat;

import android.app.Application;

public class MMApplication extends Application {

	public final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private String userID = null;
	private String userName = null;
	private String userEMail = null;

	public MMApplication() {
		super();
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEMail() {
		return userEMail;
	}

	public void setUserEMail(String userEMail) {
		this.userEMail = userEMail;
	}


}
