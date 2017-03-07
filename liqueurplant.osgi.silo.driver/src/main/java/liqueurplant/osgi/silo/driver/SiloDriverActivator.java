package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;

import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.framework.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Created by bojit on 04-Mar-17.
 */
@Component
public class SiloDriverActivator implements BundleActivator {

    private BundleContext context;
    protected static ServiceTracker serviceTracker;
    protected static InValveDriverIf inValve;
    protected static OutValveDriverIf outValve;

    @Override
    public void start(BundleContext context) throws Exception {
        //this.context = context;
        //serviceTracker = new ServiceTracker(context, InValveDriverIf.class.getName(), null);
        //serviceTracker.addingService(context.getServiceReference(OutValveDriverIf.class.getName()));
        //serviceTracker.open();

        /*
        serviceListener = event -> {
            ServiceReference inValveService = event.getServiceReference();
            switch (event.getType()) {
                case ServiceEvent.REGISTERED: {
                    inValve = (InValveDriverIf) context.getService(inValveService);
                    }
                    break;
                default:
                    break;
            }
        };
        String filter = "(objectclass=" + InValveDriverIf.class.getName() + ")";
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
        //    inValveService = context.getServiceReference(InValveDriverIf.class.getName());
        //}
        //inValve = (InValveDriverIf) context.getService(inValveService);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        //serviceTracker.close();
    }

    @Reference(
            name = "inValveDriver.service",
            service = InValveDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetInValve"
    )
    private void setInValve(InValveDriverIf inValve) {
        System.out.println("Binding inValve");
        this.inValve = inValve;
    }

    private void unsetInValve(InValveDriverIf inValve) {
        System.out.println("Unbind inValve");
        this.inValve = null;
    }

    @Reference(
            name = "outValveDriver.service",
            service = OutValveDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetOutValve"
    )
    private void setOutValve(OutValveDriverIf outValve) {
        System.out.println("Binding outValve");
        this.outValve = outValve;
    }

    private void unsetOutValve(OutValveDriverIf outValve) {
        System.out.println("Unbinding outValve");
        this.outValve = null;
    }
}
