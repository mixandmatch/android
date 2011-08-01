package de.metafinanz.mixnmatch.frontend.android.test;

import junit.framework.TestCase;
import de.metafinanz.mixnmatch.frontend.android.data.Location;

public class Test extends TestCase {

	public void testLocation() {
		Location loc = new Location();
		
		assertNotNull(loc);
	}



}
