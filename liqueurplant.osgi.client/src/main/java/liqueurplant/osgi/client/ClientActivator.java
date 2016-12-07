package liqueurplant.osgi.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ClientActivator implements BundleActivator {

    private SiloDevice silo;
    public static Logger LOG = LoggerFactory.getLogger(ClientActivator.class);
    private static BundleContext bundleContext;

    public static BundleContext getBundleContext() {
        return bundleContext;
    }

    @Override
    public void start(BundleContext context) throws Exception {

        this.bundleContext = context;
        silo = new SiloDevice("silo", null);
        silo.init();

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        silo = null;
    }
}
