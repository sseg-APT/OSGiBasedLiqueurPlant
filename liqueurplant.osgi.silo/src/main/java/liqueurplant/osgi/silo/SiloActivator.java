package liqueurplant.osgi.silo;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloActivator implements BundleActivator{

    private SiloDevice silo;

    @Override
    public void start(BundleContext context) throws Exception {

        silo = new SiloDevice("silo", null);
        silo.init();
        System.out.println("Hello service unavailable on HelloConsumer start");

        ///* Register command.
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "silo");
        props.put("osgi.command.function", new String[] {"fill", "empty"});
        context.registerService(SiloCommand.class.getName(), new SiloCommand(context), props);

        //*/
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        silo = null;
    }
}
