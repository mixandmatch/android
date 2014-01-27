package de.metafinanz.mixmatch.domain;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Appointment {

	private String appointmentID;
	private Date timestamp;
	private String locationID;
	private String owner;
	private Set<User> participants;
	
	public Appointment() {
	}
	
	public Appointment(String appointmentID, Date timestamp, String locationID,
			String owner, Set<User> participants) {
		super();
		this.appointmentID = appointmentID;
		this.timestamp = timestamp;
		this.locationID = locationID;
		this.owner = owner;
		this.participants = participants;
	}

	public String getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appointmentID == null) ? 0 : appointmentID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appointment other = (Appointment) obj;
		if (appointmentID == null) {
			if (other.appointmentID != null)
				return false;
		} else if (!appointmentID.equals(other.appointmentID))
			return false;
		return true;
	}

	
}
