package liqueurplant.osgi.valve.out;

import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by pBochalis on 3/6/2017.
 */
public class OutValveDriverActivator implements BundleActivator{
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(OutValveDriverIf.class.getName(), new OutValveDriver(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
