package de.metafinanz.mixnmatch.frontend.android.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.data.Location;
import de.metafinanz.mixnmatch.frontend.android.data.Location.Locations;

public class ContentLocations {

	private static final String TAG = "ContentLocations";

	private Map<Long, Location> locations = new HashMap<Long, Location>();
	private long index = 0l;

	// Private constructor prevents instantiation from other classes
	private ContentLocations() {
		// Location loc = new Location(1, "HVU");
		// locations.add(loc );
		// loc = new Location(2, "VGU");
		// locations.add(loc);
	}

	/**
	 * ContentLocationsHolder is loaded on the first execution of
	 * ContentLocations.getInstance() or the first access to
	 * ContentLocationsHolder.INSTANCE, not before.
	 */
	private static class ContentLocationsHolder {
		public static final ContentLocations INSTANCE = new ContentLocations();
	}

	public static ContentLocations getInstance() {
		return ContentLocationsHolder.INSTANCE;
	}

	/**
	 * Liefert alle Locations zurück.
	 * 
	 * @return
	 */
	public List<Location> getMatches() {
		List<Location> listResult = new ArrayList<Location>();
		listResult.addAll(locations.values());
		return listResult;
	}

	public Location getMatch(String key) {
		if (key != null)
			for (Location loc: locations.values()) {
				if (loc.getKey().equals(key)) 
					return loc;
			}
		return null;
	}

	public void setLocations(List<Location> locations) {
		for (Location loc : locations) {
			if (this.locations.containsValue(loc))
				Log.w(TAG, "Element " + loc.getKey()
						+ " already in list, skipping.");
			else
				this.locations.put(++index, loc);
		}
	}

	public Long insert(ContentValues values) {
		String key = values.getAsString(Locations.KEY);
		String label = values.getAsString(Locations.LABLE);
		String description = values.getAsString(Locations.DESCRIPTION);

		if (key == null || label == null)
			throw new IllegalArgumentException("Either key or label is null");

		Location newLoc = new Location(key, label);
		newLoc.setDescription(description);

		if (this.locations.containsValue(newLoc)) {
			Log.w(TAG, "Element " + newLoc.getKey()
					+ " already in list, skipping.");
			return null;
		} else {
			newLoc.setId(++index);
			this.locations.put(index, newLoc);
			Log.d(TAG, "location " + newLoc.getKey() + " added");
			return index;
		}
	}

	public void removeAll() {
		locations.clear();
	}

}
