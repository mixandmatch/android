package de.metafinanz.mixnmatch.frontend.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RequestMatch extends AbstractAsyncActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.request_match);

		Button btnMatch = (Button) findViewById(R.id.buttonRequestMatch);
		OnClickListener oclBtnMatches = new OnClickListener() {
			public void onClick(View v) {
				sendMatchWish(new Date(), "me", new Position(0,0));
			}
		};
		btnMatch.setOnClickListener(oclBtnMatches);
	}

	/**
	 * Sendet an den WebService die Daten fuer einen Mittagessenswunsch.
	 * 
	 * @return http return code
	 */
	public void sendMatchWish(Date when, String who, Position position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("day", when.toString());
		parameters.put("who", who);
		parameters.put("where", position);

		AsyncTask<Void, Void, Location[]> task = new RequestLocation(position).execute();
			 try {
				task.get();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		
	}

	private class RequestLocation extends AsyncTask<Void, Void, Location[]> {
		protected String TAG = "RequestLocation";
		
		private Position position;
		
		public RequestLocation(Position position) {
			super();
			this.position = position;
		}
		
		

		@Override
		protected void onPreExecute() {
			// before the network request begins, show a progress indicator
			showProgressDialog();
		}

		@Override
		protected Location[] doInBackground(Void... params) {
			
			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// The HttpComponentsClientHttpRequestFactory uses the
			// org.apache.http package to make network requests
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

			// The URL for making the GET request
//			final String url = getString(R.string.base_uri) + 
//				"locations/nearby?lat={"+position.getLatitude()+"}&lon={"+position.getLongitude()+"}";
			final String url = getString(R.string.base_uri) + "locations";
			
			// Initiate the HTTP GET request, expecting an array of State
			// objects in response
//			String result = restTemplate.getForObject(url, String.class); 
			//Man kann auch gleich ein POJO statt String erstellen lassen.
			Location[] locations = restTemplate.getForObject(url, Location[].class); 
			
			return locations;
		}

		@Override
		protected void onPostExecute(Location[] locations) {
			// hide the progress indicator when the network request is complete
			dismissProgressDialog();

			if (locations != null && locations.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < locations.length; i++) {
					sb.append(locations[i].getLabel());
					sb.append(" (");
					sb.append(locations[i].getKey());
					sb.append(")");
					sb.append("\n");
				}
				Toast toast = Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG);
				toast.show();
			}
		}
	}

}
