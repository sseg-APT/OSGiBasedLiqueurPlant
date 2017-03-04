package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.valve.api.ValveIf;
import org.apache.felix.scr.impl.manager.ServiceTracker;
import org.osgi.framework.*;

/**
 * Created by bojit on 04-Mar-17.
 */
public class SiloDriverActivator implements BundleActivator {

    //private BundleContext context;
    protected static ValveIf inValve;
    private ServiceListener serviceListener;

    @Override
    public void start(BundleContext context) throws Exception {
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
        //this.context = context;
        //ServiceReference inValveService = null;
        //while(inValveService == null){
        //    inValveService = context.getServiceReference(ValveIf.class.getName());
        //}
        //inValve = (ValveIf) context.getService(inValveService);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
