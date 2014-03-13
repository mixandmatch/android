package de.metafinanz.mixmatch.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import android.content.Context;
import android.util.Log;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.JSONAppointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class RestDataService implements IDataService {
	
	private static final RestDataService instance = new RestDataService();
	private RestTemplate restTemplate;
	private String baseUrl;
	private static Context ctx;
	private static String REST_URL_LOCATIONS = "locations";
	private static String REST_URL_USER = "users";
	private static String REST_URL_APPOINTMENTS = "appointments";
	private static String REST_URL_APPOINTMENTS_FOR_LOCATION = "appointments/location?locationID={id}";
	private static String REST_URL_APPOINTMENTS_FOR_USER = "appointments/user?userID={id}";
	private MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	
	private List<Location> locationList = new ArrayList<Location>();
	
	private RestDataService() {
		this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(converter);        
	}
	
	public static RestDataService getInstance(String baseUrl) {
		instance.baseUrl = baseUrl;
		return instance;
	}
	
	public static RestDataService getInstance(Context context) {
		if (ctx == null) {
			ctx = context;
			instance.baseUrl = ctx.getString(R.string.restServiceURL);
		}		
		return instance;
	}

	@Override
	public List<Location> getLocations() {
		Location[] locations = restTemplate.getForObject(baseUrl + REST_URL_LOCATIONS, Location[].class);
		this.locationList = Arrays.asList(locations);
		return this.locationList;
	}

	@Override
	public Location getLocationById(Long id) {
		Location result = null;
		
		for(Location location: this.locationList) {
			if (location.getLocationID().equals(id)) {
				result = location;
				break;
			}
		}
		return result;
	}

	@Override
	public List<User> getParticipantsByAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Appointment> getAppointmentsByLocation(Location location) {
		String url = baseUrl + REST_URL_APPOINTMENTS_FOR_LOCATION;
		Appointment[] appointments = restTemplate.getForObject(url, Appointment[].class, location.getLocationID());
		Log.i("RestDataService", "" + appointments);
		
		if (appointments != null) {
			return Arrays.asList(appointments);
		} else {
			return new ArrayList<Appointment>();
		}
		
	}

	@Override
	public List<Appointment> getAppointmentsByLocationId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Appointment> getAppointmentsByUsername(String username) {
		String url = baseUrl + REST_URL_APPOINTMENTS_FOR_USER;
		User user = getOrCreateUser(username);
		Appointment[] appointments = restTemplate.getForObject(url, Appointment[].class, user.getId());
		Log.i("RestDataService", "" + appointments);
		
		if (appointments != null) {
			return Arrays.asList(appointments);
		} else {
			return new ArrayList<Appointment>();
		}
	}

	@Override
	public Appointment createNewAppointment(Appointment appointment) {
		return handleJSON(appointment);	
	}
	
	@Override
	public User getOrCreateUser(String username) {
		User user = new User(username);
		ResponseEntity<User> response = restTemplate.postForEntity(this.baseUrl + REST_URL_USER, user, User.class);
		User userCreated = response.getBody();		
		return userCreated;
	}
	
	public List<Appointment> getAppointments() {
		Appointment[] appointments = restTemplate.getForObject(baseUrl + REST_URL_APPOINTMENTS, Appointment[].class);
		return Arrays.asList(appointments);
	}
	
	@Override
	public Appointment getAppointmentById(Long appointmentId) {
		List<Appointment> appList = getAppointments();
		for (Appointment appointment : appList) {
			if (appointment.getAppointmentID().equals(appointmentId)) {
				return appointment;
			}
		}
		return null;
	}
	
	private Appointment handleJSON(Appointment appointment) {
		JSONAppointment jsonApp = new JSONAppointment();
		jsonApp.setAppointmentDate(appointment.getAppointmentDate());
		jsonApp.setAppointmentLocation(appointment.getAppointmentLocation().getLocationID());
		User user = getOrCreateUser(appointment.getOwnerID().getUsername());
		jsonApp.setOwnerID(user.getId());
		Log.i("Rest", "Create Appointment: " + jsonApp.getAppointmentDate());
        ResponseEntity<Appointment> response = restTemplate.postForEntity(this.baseUrl + REST_URL_APPOINTMENTS, jsonApp, Appointment.class);
        //exchange(this.baseUrl + "appointments", HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
	}
	
	private void handleString() {
        String response = restTemplate.postForObject(this.baseUrl + "appointments", "100", String.class);
	}
	
	
	
}
