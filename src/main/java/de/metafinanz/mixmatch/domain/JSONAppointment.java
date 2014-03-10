package de.metafinanz.mixmatch.domain;

import java.util.Date;



/**
 * Used for deserialization of an appointment only containing ID references to
 * the owner and location.
 * 
 * @see Appointment#fromJsonToAppointment(String)
 */

public class JSONAppointment {

	private Long appointmentID;

	private Date appointmentDate;

	private Long ownerID;

	private Long appointmentLocation;

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

	public Long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public Long getAppointmentLocation() {
		return appointmentLocation;
	}

	public void setAppointmentLocation(Long appointmentLocation) {
		this.appointmentLocation = appointmentLocation;
	}
	
	
}
