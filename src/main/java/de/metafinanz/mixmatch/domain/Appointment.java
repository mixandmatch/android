package de.metafinanz.mixmatch.domain;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Appointment {

	private Long appointmentID;
	private Set<User> participants;
	private Date appointmentDate;

	private User ownerID;
	private Location appointmentLocation;
	
	public Appointment() {
	}
	
	public Appointment(Long appointmentID, Date appointmentDate, Location location,
			User owner, Set<User> participants) {
		super();
		this.appointmentID = appointmentID;
		this.appointmentDate = appointmentDate;
		this.appointmentLocation = location;
		this.ownerID = owner;
		this.participants = participants;
	}

	public Long getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(Long appointmentID) {
		this.appointmentID = appointmentID;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public User getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(User ownerID) {
		this.ownerID = ownerID;
	}

	public Location getAppointmentLocation() {
		return appointmentLocation;
	}

	public void setAppointmentLocation(Location appointmentLocation) {
		this.appointmentLocation = appointmentLocation;
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
