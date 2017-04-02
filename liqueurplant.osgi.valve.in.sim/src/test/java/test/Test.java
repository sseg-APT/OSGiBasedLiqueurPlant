package test;

import junit.framework.TestCase;
import liqueurplant.osgi.valve.in.sim.InValveDriver;

/**
 * Created by bojit on 02-Apr-17.
 */
public class Test extends TestCase {

    public void testSimple() throws Exception {
        InValveDriver t = new InValveDriver();
        assertEquals( t.test(), "Test ok.");
    }

}