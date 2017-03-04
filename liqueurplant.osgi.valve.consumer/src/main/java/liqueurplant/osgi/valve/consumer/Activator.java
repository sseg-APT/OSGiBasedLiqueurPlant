package liqueurplant.osgi.valve.consumer;

import liqueurplant.osgi.valve.api.ValveIf;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Created by bojit on 03-Mar-17.
 */
public class Activator implements BundleActivator {

    private BundleContext context;
    private ValveIf valve;
    private ServiceTracker tracker;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        //ServiceReference reference = context.getServiceReference(ValveIf.class.getName());
        //valve = (ValveIf) context.getService(reference);
        //valve.open();
        tracker = new ServiceTracker(context, ValveIf.class.getName(), null);
        tracker.open();
        ((ValveIf)tracker.getService()).open();

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        tracker.close();
    }

}
