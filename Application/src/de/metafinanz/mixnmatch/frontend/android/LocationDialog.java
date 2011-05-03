package de.metafinanz.mixnmatch.frontend.android;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.Bundle;


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
