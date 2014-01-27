package de.metafinanz.mixmatch.service;

import java.util.List;

import android.content.Context;

import de.metafinanz.mixmatch.activities.MixMatchActivity;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class MixMatchService {
	
	public static final String DATA_SERVICE_REST = "rest";
	public static final String DATA_SERVICE_LOCAL = "local";
	public static final String DATA_SERVICE_KEY = "dataService";
	
	private static final MixMatchService instance = new MixMatchService();
	
	private static IDataService dataservice;
	
	private MixMatchService() {
	}

	public static MixMatchService getInstance(Context ctx) {
		init(ctx);
		return instance;
	}
	
	public static void init(Context ctx) {
		String ds = ctx.getSharedPreferences(MixMatchActivity.MIXMATCH_PREFS, MixMatchActivity.MODE_PRIVATE).getString("dataService", "rest");
		if (DATA_SERVICE_LOCAL.equals(ds)) {
			dataservice = DataService.getInstance();
		} else {
			dataservice = RestDataService.getInstance(ctx);
		}
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
	
	public Location getLocationById(String id) {
		return dataservice.getLocationById(id);
	}

	public String createNewAppointment(Appointment appointment) {
		return dataservice.createNewAppointment(appointment);		
	}
	
}
