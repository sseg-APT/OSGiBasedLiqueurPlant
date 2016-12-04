package liqueurplant.valve;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by bochalito on 4/12/2016.
 */
public class ValveActivator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Valve in = new Valve("IN");
        in.open();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
