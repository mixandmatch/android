package de.metafinanz.mixmatch.service;

import java.util.List;

import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class MixMatchService {
	
	private static final MixMatchService instance = new MixMatchService();
	
	DataService dataservice = DataService.getInstance();
	
	private MixMatchService() {
	}

	public static MixMatchService getInstance() {
		return instance;
	}
	
	
	public List<Appointment> getAppointments(String username) {
		return dataservice.getAppointmentsByUsername(username);
	}
	
	public List<Appointment> getAppointments(Location location) {
		return dataservice.getAppointmentsByLocation(location);
	}
	
	
	public List<User> getParticipants(Appointment appointment) {
		return dataservice.getParticipantsByAppointment(appointment);
	}
	
	public List<Location> getLocations() {
		return dataservice.getLocations();
	}
	
}
