package liqueurplant.osgi.valve.consumer;

import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Created by bojit on 03-Mar-17.
 */
public class Activator implements BundleActivator {

    private BundleContext context;
    private ValveIf valve;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        ServiceReference reference = context.getServiceReference(ValveIf.class.getName());
        valve = (ValveIf) context.getService(reference);
        valve.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
