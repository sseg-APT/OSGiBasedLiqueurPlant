package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.valve.api.ValveIf;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Created by bojit on 04-Mar-17.
 */

public class SiloDriverActivator implements BundleActivator {

    private BundleContext context;
    protected static ValveIf inValve;
    protected static ServiceTracker serviceTracker;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        serviceTracker = new ServiceTracker(context, ValveIf.class.getName(), null);

        serviceTracker.open();

        /*
        serviceListener = event -> {
            ServiceReference inValveService = event.getServiceReference();
            switch (event.getType()) {
                case ServiceEvent.REGISTERED: {
                    inValve = (ValveIf) context.getService(inValveService);
                    }
                    break;
                default:
                    break;
            }
        };
        String filter = "(objectclass=" + ValveIf.class.getName() + ")";
        try {
            context.addServiceListener(serviceListener, filter);
        } catch (InvalidSyntaxException ex) {
            ex.printStackTrace();
        }
        //*/
        // Service Reference
        //this.context = context;
        //ServiceReference inValveService = null;
        //while(inValveService == null){
        //    inValveService = context.getServiceReference(ValveIf.class.getName());
        //}
        //inValve = (ValveIf) context.getService(inValveService);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        serviceTracker.close();
    }


}
