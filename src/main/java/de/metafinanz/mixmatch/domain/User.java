package de.metafinanz.mixmatch.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * In this first version a user doesn't have a account. Therefore only a
 * username is needed.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

	private String username;
	private Long id;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String username) {
		this.username = username;
	}
	
	@Override
	public boolean equals(Object aObject) {
		if (aObject instanceof User) {
			return username.equals(((User) aObject).getUsername());
		}
		return super.equals(aObject);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

}
