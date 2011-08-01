package de.metafinanz.mixnmatch.frontend.android.test.services;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import de.metafinanz.mixnmatch.frontend.android.services.DataService;

public class DataServiceTests extends ServiceTestCase<DataService> {
	private static final String TAG = "DataServiceTests";

	public DataServiceTests() {
		super(DataService.class);
	}

    /**
     * The name 'test preconditions' is a convention to signal that if this
     * test doesn't pass, the test case was not set up properly and it might
     * explain any and all failures in other tests.  This is not guaranteed
     * to run before other tests, as junit uses reflection to find the tests.
     */
    @SmallTest
    public void testPreconditions() {
    }
    
    /**
     * Test basic startup/shutdown of Service
     */
    @SmallTest
    public void testStartable() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), DataService.class);
        startService(startIntent); 

        assertNotNull(getService());

    }


}
