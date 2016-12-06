package liqueurplant.osgi.silo;

import liqueurplant.osgi.valve.Valve;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloCommand {

    BundleContext bundleContext;
    private Silo testSilo;

    public SiloCommand(BundleContext bundleContext){
        this.bundleContext =bundleContext;
        testSilo = new Silo();
        testSilo.setInValve(new Valve("IN-TEST"));
        testSilo.setOutValve(new Valve("OUT-TEST"));

    }

    public void fill(){
        try {
            testSilo.fill();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void empty(){
        try {
            testSilo.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateValve(){
        Bundle bundle = bundleContext.getBundle(5);
        try {
            SiloActivator.LOG.debug("Trying bundle " + bundle.getSymbolicName() + " update...");
            bundle.update();
            SiloActivator.LOG.debug("Bundle updated.");
        } catch (BundleException e) {
            SiloActivator.LOG.debug("Cannot update bundle.");
            e.printStackTrace();
        }
    }
}