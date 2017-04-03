package test;

import liqueurplant.osgi.valve.in.sim.InValveDriver;
import org.junit.Assert;

/**
 * Created by bojit on 02-Apr-17.
 */
public class Test {

    @org.junit.Test
    public void testSimple() throws Exception {
        InValveDriver t = new InValveDriver();
        Assert.assertEquals( t.test(), "Test ok.");
    }

}