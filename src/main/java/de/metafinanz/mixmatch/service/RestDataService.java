package de.metafinanz.mixmatch.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.util.Log;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.domain.Appointment;
import de.metafinanz.mixmatch.domain.Location;
import de.metafinanz.mixmatch.domain.User;

public class RestDataService implements IDataService {
	
	private static final RestDataService instance = new RestDataService();
	private RestTemplate restTemplate;
	private String baseUrl;
	private static Context ctx;
	
	private List<Location> locationList = new ArrayList<Location>();
	
	private RestDataService() {
		this.restTemplate = new RestTemplate();
		this.restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());        
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
		Location[] locations = restTemplate.getForObject(baseUrl + "locations", Location[].class);
		this.locationList = Arrays.asList(locations);
		return this.locationList;
	}

	@Override
	public Location getLocationById(String id) {
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
		Appointment[] appointments = restTemplate.getForObject(baseUrl + "appointments?locationId=" + location.getLocationID(), Appointment[].class);
		Log.i("RestDataService", "" + appointments);
		
		if (appointments != null) {
			return Arrays.asList(appointments);
		} else {
			return new ArrayList<Appointment>();
		}
		
	}

	@Override
	public List<Appointment> getAppointmentsByLocationId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Appointment> getAppointmentsByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createNewAppointment(Appointment appointment) {
		handleJSON(appointment);	
	}
	
	private void handleJSON(Appointment appointment) {
		HttpHeaders requestHeaders = new HttpHeaders();
        // Sending a JSON or XML object i.e. "application/json" or "application/xml"
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Appointment> requestEntity = new HttpEntity<Appointment>(appointment, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(this.baseUrl + "appointments", HttpMethod.POST, requestEntity,
                String.class);
	}
	
	private void handleString() {
        String response = restTemplate.postForObject(this.baseUrl + "appointments", "100", String.class);
	}
	
}
