package liqueurplant.osgi.valve;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by bocha on 6/12/2016.
 */
public class ValveActivator implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Valve started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Valve stopped.");
    }
}
