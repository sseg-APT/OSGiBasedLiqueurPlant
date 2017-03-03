package liqueurplant.osgi.silo.controller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;

/**
 * Created by bojit on 03-Mar-17.
 */
public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "silo");
        props.put("osgi.command.function", "run");
        context.registerService(SimpleSiloCtrl.class.getName(), new SimpleSiloCtrl(SimpleSiloCtrlState.EMPTY), props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
