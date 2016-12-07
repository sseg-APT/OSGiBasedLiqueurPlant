package liqueurplant.osgi.silo;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloActivator implements BundleActivator {


    public static Logger LOG = LoggerFactory.getLogger(SiloActivator.class);
    private static BundleContext bundleContext;

    public static BundleContext getBundleContext() {
        return bundleContext;
    }

    @Override
    public void start(BundleContext context) throws Exception {

        this.bundleContext = context;

        //* Register command.
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "silo");
        props.put("osgi.command.function", new String[]{"fill", "empty", "updateValve"});
        context.registerService(SiloCommand.class.getName(), new SiloCommand(context), props);
        //Bundle bundle = ctx.getBundle(5);
        //System.out.println(bundle.getSymbolicName());
        //bundle.update();

        //*/
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }
}
