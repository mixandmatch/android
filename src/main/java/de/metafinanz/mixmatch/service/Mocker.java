package de.metafinanz.mixmatch.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class Mocker {

	public Set<Location> locations;
	public Set<User> users;
	public Set<Appointment> appointments;
	
	public Mocker() {
		List<User> users = getMockedUsers();
		this.users = new HashSet<User>(users);
		List<Location> locations = getMockedLocations();
		this.locations = new HashSet<Location>(locations);
		
		appointments = new HashSet<Appointment>();
		
		Appointment app1 = new Appointment();
		app1.setAppointmentID(1L);
		app1.setOwnerID(users.get(0));
		app1.setAppointmentDate(getToday());
		app1.setAppointmentLocation(locations.get(0));
		app1.setParticipants(new HashSet<User>());
		for(int i = 0; i < 10; i++) {
			app1.getParticipants().add(users.get(i));
		}
		appointments.add(app1);
		
		Appointment app2 = new Appointment();
		app2.setAppointmentID(2L);
		app2.setOwnerID(users.get(0));
		app2.setAppointmentDate(getToday());
		app2.setAppointmentLocation(locations.get(0));
		app2.setParticipants(new HashSet<User>());
		for(int i = 10; i < 20; i++) {
			app2.getParticipants().add(users.get(i));
		}
		appointments.add(app2);
		
		Appointment app3 = new Appointment();
		app3.setAppointmentID(3L);
		app3.setOwnerID(users.get(0));
		app3.setAppointmentDate(getToday());
		app3.setAppointmentLocation(locations.get(0));
		app3.setParticipants(new HashSet<User>());
		for(int i = 0; i < 20; i+=2) {
			app3.getParticipants().add(users.get(i));
		}
		appointments.add(app3);
		
		
	}
	
	
	private List<User> getMockedUsers() {
		ArrayList<User> users = new ArrayList<User>();
		for(int i = 0; i < 20; i++) {
			User user = new User();
			user.setUsername("Username" + i);
			users.add(user);
		}
		return users;
	}

	private List<Location> getMockedLocations() {
		ArrayList<Location> locations = new ArrayList<Location>();
		for(int i = 0; i < 5; i++) {
			Location location = new Location();
			location.setLocationID(Long.valueOf(i));
			location.setLocationName("Location:" + i);
			locations.add(location);
		}
		return locations;
	}
	
	
	public Date getToday() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, 12);
		c.set(Calendar.MINUTE, 00);
		return c.getTime();
	}
	
	public Date getTomorrow() {
		Calendar c = Calendar.getInstance(); 
		c.setTime(getToday()); 
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
}
