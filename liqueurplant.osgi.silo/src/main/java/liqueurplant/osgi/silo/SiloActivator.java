package liqueurplant.osgi.silo;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloActivator implements BundleActivator{

    private SiloDevice silo;
    public static Logger LOG = LoggerFactory.getLogger(SiloActivator.class);

    @Override
    public void start(BundleContext context) throws Exception {


        final BundleContext ctx = context;
        silo = new SiloDevice("silo", null);
        silo.init();
        LOG.debug("Hello service unavailable on HelloConsumer start");

        ///* Register command.
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "silo");
        props.put("osgi.command.function", new String[] {"fill", "empty", "updateValve"});
        context.registerService(SiloCommand.class.getName(), new SiloCommand(context), props);
        //Bundle bundle = ctx.getBundle(5);
        //System.out.println(bundle.getSymbolicName());
        //bundle.update();

        //*/
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        silo = null;
    }
}