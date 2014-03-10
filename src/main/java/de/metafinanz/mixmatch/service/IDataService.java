package de.metafinanz.mixmatch.service;

import java.util.List;

import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public interface IDataService {
	
	List<Location> getLocations();
	Location getLocationById(Long id);
	List<User>getParticipantsByAppointment(Appointment appointment);
	List<Appointment> getAppointmentsByLocation(Location location);
	List<Appointment> getAppointmentsByLocationId(Long id);
	List<Appointment> getAppointmentsByUsername(String username);
	Appointment createNewAppointment(Appointment appointment);
	User getOrCreateUser(String username);

}
