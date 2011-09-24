package de.metafinanz.mixnmatch.frontend.android.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.google.gson.Gson;

import de.metafinanz.mixnmatch.frontend.android.data.Request;

public class RequestMessageConverter extends AbstractHttpMessageConverter<Request> {

	@Override
	protected boolean supports(Class<?> clazz) {
		return Request.class.equals(clazz);
	}

	@Override
	protected Request readInternal(Class<? extends Request> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

        Gson gson = new Gson();
        Reader reader = new InputStreamReader(inputMessage.getBody());
        Request request = gson.fromJson(reader, Request.class);
        
        
		return request;
	}

	

	@Override
	protected void writeInternal(Request t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// TODO Auto-generated method stub
		
	}

}
