package de.metafinanz.mixnmatch.frontend.android;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;


public class LocationDialog extends MapActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_dialog);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
