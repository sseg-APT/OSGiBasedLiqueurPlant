package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by bojit on 03-Mar-17.
 */
public class ActivatorValve implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        context.registerService(ValveIf.class.getName(), new ValveDriver(), null);
        System.out.println("Service valve registered");
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
