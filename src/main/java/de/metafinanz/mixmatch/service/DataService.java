package de.metafinanz.mixmatch.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class DataService implements IDataService {
	
	private static final DataService instance = new DataService();
	private AtomicLong idCounter = new AtomicLong(1);

	Mocker mocker = new Mocker();

	private Map<Long, List<Appointment>> appointmentMap = new HashMap<Long, List<Appointment>>();
	private List<Appointment> appointmentList = new ArrayList<Appointment>();
	
	private DataService() {
		Set<User> participants = new HashSet<User>();
		Location loc = new Location();
		loc.setLocationID(1L);
		User user1 = new User();
		user1.setUsername("ujr");
		this.appointmentList.add(new Appointment(1L, new Date(), loc, user1, participants));
		this.appointmentMap.put(1L, appointmentList);
	}
	
	public static DataService getInstance() {
		return instance;
	}

	public List<Location> getLocations() {
		// TODO Remove Mocker
		return new ArrayList<Location>(mocker.locations);
	}
	
	public Location getLocationById(Long id) {
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
		return getAppointmentsByLocationId(location.getLocationID());
	}
	
	public List<Appointment> getAppointmentsByLocationId(Long locationId) {
		List<Appointment> result = new ArrayList<Appointment>();
		
		if (locationId != null) {
			result = this.appointmentMap.get(locationId);
		}
		
		if (result != null) {
			return result;
		} else {
			return new ArrayList<Appointment>();
		}
	}

	public List<Appointment> getAppointmentsByUsername(String username) {
		List<Appointment> result = new ArrayList<Appointment>();
		
		Iterator<List<Appointment>> iter = this.appointmentMap.values().iterator();
		
		if (username != null) {
			while(iter.hasNext()) {
				for(Appointment app : iter.next()) {
					if (app.getOwnerID().getUsername().equals(username) || app.getParticipants().contains(username)) {
						result.add(app);
					}
				}
			}
		}
		
		return result;
	}
	@Override
	public Appointment createNewAppointment(Appointment appointment) {
		
		List<Appointment> appList = this.appointmentMap.get(appointment.getAppointmentLocation().getLocationID());
		
		appointment.setAppointmentID(idCounter.incrementAndGet());
		
		if (appList != null) {
			appList.add(appointment);
			this.appointmentMap.put(appointment.getAppointmentLocation().getLocationID(), appList);
		} else {
			List<Appointment> newAppList = new ArrayList<Appointment>();
			newAppList.add(appointment);
			this.appointmentMap.put(appointment.getAppointmentLocation().getLocationID(), newAppList);
		}		
		
		return appointment;
		
	}
	
	@Override
	public User getOrCreateUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Appointment getAppointmentById(Long appointmentId) {
		// TODO Auto-generated method stub
		return null;
	}
}
