package de.metafinanz.mixnmatch.frontend.android.providers;

import java.util.ArrayList;
import java.util.List;

import de.metafinanz.mixnmatch.frontend.android.Location;


public class ContentLocations {
	
	private List<Location> locations = new ArrayList<Location>();

	// Private constructor prevents instantiation from other classes
	private ContentLocations() {
	}

	/**
	 * ContentLocationsHolder is loaded on the first execution of
	 * ContentLocations.getInstance() or the first access to ContentLocationsHolder.INSTANCE,
	 * not before.
	 */
	private static class ContentLocationsHolder {
		public static final ContentLocations INSTANCE = new ContentLocations();
	}

	public static ContentLocations getInstance() {
		return ContentLocationsHolder.INSTANCE;
	}

	/**
	 * Liefert je nach Query eine Auswahl an Locations zurück. Die Query wird momentan aber nicht ausgewertet.
	 * @param processedQuery
	 * @return
	 */
	public List<Location> getMatches(String processedQuery) {
		return locations;
	}


}
