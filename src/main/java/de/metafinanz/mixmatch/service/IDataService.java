package de.metafinanz.mixmatch.service;

import java.util.List;

import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public interface IDataService {
	
	List<Location> getLocations();
	Location getLocationById(String id);
	List<User>getParticipantsByAppointment(Appointment appointment);
	List<Appointment> getAppointmentsByLocation(Location location);
	List<Appointment> getAppointmentsByLocationId(String id);
	List<Appointment> getAppointmentsByUsername(String username);
	void createNewAppointment(Appointment appointment);

}
