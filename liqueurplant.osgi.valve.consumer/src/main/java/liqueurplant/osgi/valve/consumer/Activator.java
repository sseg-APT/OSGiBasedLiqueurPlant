package liqueurplant.osgi.valve.consumer;

import liqueurplant.osgi.valve.in.api.ValveIf;
import org.osgi.annotation.versioning.ConsumerType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;

/**
 * Created by bojit on 03-Mar-17.
 */
@Component

public class Activator implements BundleActivator {

    private ValveIf valveService;

    @Override
    public void start(BundleContext context) throws Exception {
        valveService.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
