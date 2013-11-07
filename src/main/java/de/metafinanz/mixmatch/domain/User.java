package de.metafinanz.mixmatch.domain;

/**
 * In this first version a user doesn't have a account. Therefore only a
 * username is needed.
 * 
 */
public class User {

	private String userID;
	private String username;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
