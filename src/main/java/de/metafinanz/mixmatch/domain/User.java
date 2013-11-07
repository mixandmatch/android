package de.metafinanz.mixmatch.domain;

import java.util.Set;

/**
 * In this first version a user doesn't have a account. Therefore only a
 * username is needed.
 * 
 */
public class User {

	private String username;
	private Set<Appointment> appointments;

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

	public Set<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}

}
