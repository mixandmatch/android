package de.metafinanz.mixmatch.service;

import java.util.ArrayList;
import java.util.List;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class DataService implements IDataService {
	
	private static final DataService instance = new DataService();

	Mocker mocker = new Mocker();
	
	private DataService() {
	}
	
	public static DataService getInstance() {
		return instance;
	}

	public List<Location> getLocations() {
		// TODO Remove Mocker
		return new ArrayList<Location>(mocker.locations);
	}
	
	public Location getLocationById(String id) {
		Location result = null;
		for(Location location: getLocations()) {
			if (location.getLocationID().equals(id)) {
				result = location;
				break;
			}
		}
		return result;
	}

	public List<User> getParticipantsByAppointment(Appointment appointment) {
		// TODO Remove Mocker
		List<User> users = new ArrayList<User>();
		for(Appointment app : mocker.appointments) {
			if(app.equals(appointment)) {
				users.addAll(app.getParticipants());
				break;
			}
		}
		return users;
	}

	public List<Appointment> getAppointmentsByLocation(Location location) {
		// TODO Remove Mocker
		List<Appointment> appointments = new ArrayList<Appointment>();
		for(Appointment app : mocker.appointments) {
			if(app.getLocationID().equals(location.getLocationID())) {
				appointments.add(app);
			}
		}
		return appointments;
	}
	
	public List<Appointment> getAppointmentsByLocationId(String locationId) {
		// TODO Remove Mocker
		List<Appointment> appointments = new ArrayList<Appointment>();
		for(Appointment app : mocker.appointments) {
			if(app.getLocationID().equals(locationId)) {
				appointments.add(app);
			}
		}
		return appointments;
	}

	public List<Appointment> getAppointmentsByUsername(String username) {
		// TODO Remove Mocker
		List<Appointment> appointments = new ArrayList<Appointment>();
		for(Appointment app : mocker.appointments) {
			for(User u : app.getParticipants()) {
				if(u.getUsername().equals(username)) {
					appointments.add(app);
					break;
				}
			}
		}
		return appointments;
	}
	@Override
	public void createNewAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		
	}
}
