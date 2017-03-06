package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.in.api.ValveIf;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by bojit on 06-Mar-17.
 */
public class Acivator implements BundleActivator{
    @Override
    public void start(BundleContext context) throws Exception {
        context.registerService(ValveIf.class.getName(), new ValveDriver(), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
