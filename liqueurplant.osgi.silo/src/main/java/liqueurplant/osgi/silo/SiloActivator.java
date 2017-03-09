package liqueurplant.osgi.silo;

import liqueurplant.osgi.silo.controller.SimpleSiloCtrl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bojit on 03-Mar-17.
 */
public class SiloActivator implements BundleActivator {

    private SimpleSiloCtrl simpleSiloCtrl;
    private ArrayBlockingQueue<SiloCtrlEvent> siloCtrlEventQueue;
    private SiloProcess siloProcess;
    SiloActivator silo;


    @Override
    public void start(BundleContext context) throws Exception {
        siloCtrlEventQueue = new ArrayBlockingQueue<SiloCtrlEvent>(20);
        siloProcess = new SiloProcess(siloCtrlEventQueue);
        simpleSiloCtrl = new SimpleSiloCtrl(SimpleSiloCtrlState.EMPTY, siloCtrlEventQueue);
        new Thread(siloProcess).start();
        new Thread(simpleSiloCtrl).start();

    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
