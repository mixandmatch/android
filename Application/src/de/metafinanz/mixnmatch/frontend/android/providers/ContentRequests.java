package de.metafinanz.mixnmatch.frontend.android.providers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.util.Log;
import de.metafinanz.mixnmatch.frontend.android.data.Request;
import de.metafinanz.mixnmatch.frontend.android.data.Request.Requests;

public class ContentRequests {

	private static final String TAG = "ContentRequests";

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	private Map<Date, Request> requests = new HashMap<Date, Request>();

	// Private constructor prevents instantiation from other classes
	private ContentRequests() {
		// Request loc = new Request(1, "HVU");
		// requests.add(loc );
		// loc = new Request(2, "VGU");
		// requests.add(loc);
	}

	/**
	 * ContentRequestsHolder is loaded on the first execution of
	 * ContentRequests.getInstance() or the first access to
	 * ContentRequestsHolder.INSTANCE, not before.
	 */
	private static class ContentRequestsHolder {
		public static final ContentRequests INSTANCE = new ContentRequests();
	}

	public static ContentRequests getInstance() {
		return ContentRequestsHolder.INSTANCE;
	}

	/**
	 * Liefert je nach Query eine Auswahl an Requests zurück. Die Query wird
	 * momentan aber nicht ausgewertet.
	 * 
	 * @param processedQuery
	 * @return
	 */
	public List<Request> getMatches(String processedQuery) {
		List<Request> listResult = new ArrayList<Request>();
		listResult.addAll(requests.values());
		return listResult;
	}

	public Request getMatch(Long index) {
		return requests.get(index);
	}

	public void setRequests(List<Request> requests) {
		for (Request req : requests) {
			if (this.requests.containsValue(req))
				Log.w(TAG, "Element " + sdf.format(req.getDate())
						+ " already in list, skipping.");
			else
				this.requests.put(req.getDate(), req);
		}
	}

	public Date insert(ContentValues values) {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		String locationKey = values.getAsString(Requests.LOCATION_KEY);
		String tmpdate = values.getAsString(Requests.DATE);
		Date date;
		try {
			date = sdf.parse(tmpdate);
		} catch (ParseException e) {
			Log.e(TAG, "Fehler beim parsen des Datums. Verwende 1.1.1970.");
			date = new Date(0);
		}
		String userId = values.getAsString(Requests.USER_ID);

		if (locationKey == null || date == null || userId == null)
			throw new IllegalArgumentException("One field is null.");
		
		Request newReq = new Request(locationKey, date, userId);

		if (this.requests.containsValue(newReq)) {
			Log.w(TAG, "Element for date " + newReq.getDate()
					+ " already in list, skipping.");
			return null;
		} else {
			this.requests.put(newReq.getDate(), newReq);
			Log.d(TAG, "request for date " + newReq.getDate() + " added");
			return newReq.getDate();
		}
	}

	public void removeAll() {
		requests.clear();
	}

}
