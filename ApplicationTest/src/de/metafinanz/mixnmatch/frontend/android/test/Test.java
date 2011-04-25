package de.metafinanz.mixnmatch.frontend.android.test;

import de.metafinanz.mixnmatch.frontend.android.Location;
import junit.framework.TestCase;

public class Test extends TestCase {

	public void testLocation() {
		Location loc = new Location();
		
		assertNotNull(loc);
	}

}
