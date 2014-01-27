package de.metafinanz.mixmatch;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import de.metafinanz.mixmatch.domain.Location;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws HttpMessageNotWritableException 
	 */
	public static void main(String[] args) throws HttpMessageNotWritableException, IOException {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		System.out.println(converter.canRead(String.class, null));
		
		Location loc = new Location();
		loc.setLocationID("125");
		
		HttpOutputMessage message = new HttpOutputMessage() {
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
			@Override
			public HttpHeaders getHeaders() {
				// TODO Auto-generated method stub
				return new HttpHeaders();
			}
			
			@Override
			public OutputStream getBody() throws IOException {
				// TODO Auto-generated method stub
				return stream;
			}
		};
		converter.write(loc, null, message);
		System.out.println(message.getBody());
		
		loc.setLocationID("123");
		loc.setLocationName("test");
		String json = converter.getObjectMapper().writeValueAsString(loc);
		System.out.println(json);
		
		RestTemplate temp = new RestTemplate(true);
	}

}
