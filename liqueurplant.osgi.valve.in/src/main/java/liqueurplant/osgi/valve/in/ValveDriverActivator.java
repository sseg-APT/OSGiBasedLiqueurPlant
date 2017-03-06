package liqueurplant.osgi.valve.in;

import liqueurplant.osgi.valve.in.api.ValveIf;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by bojit on 04-Mar-17.
 */
public class ValveDriverActivator implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        context.registerService(ValveIf.class.getName(), new InValveDriver(), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }
}
