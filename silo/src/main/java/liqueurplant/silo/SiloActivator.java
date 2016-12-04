package liqueurplant.silo;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloActivator implements BundleActivator{

    private SiloDevice silo;

    @Override
    public void start(BundleContext context) throws Exception {
        //silo = new liqueurplant.silo.leshan.silo.Silo("Silo", null);
        //silo.init();

        silo = new SiloDevice("silo", null);
        silo.init();
        System.out.println("Hello service unavailable on HelloConsumer start");
        /* Register command.
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "silo");
        props.put("osgi.command.function", new String[] {"fill", "empty"});
        context.registerService(SiloCommand.class.getName(), new SiloCommand(context), props);

        serviceTracker = new ServiceTracker(context, Valve.class.getName(), null);
        serviceTracker.open();
        Valve valve = (Valve) serviceTracker.getService();

        if (valve == null) {
            System.out.println("Hello service unavailable on HelloConsumer start");
        } else {
            System.out.println("yeeeeee");
            valve.open();
        }
        */
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        //silo = null;
        // Valve valve = (Valve) serviceTracker.getService();
        // serviceTracker.close();
    }
}
