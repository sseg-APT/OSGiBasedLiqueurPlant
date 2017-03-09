package liqueurplant.osgi.plant;

import liqueurplant.osgi.silo.controller.SimpleSiloCtrl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by bocha on 20/2/2017.
 */
public class zLiqueurPlant implements BundleActivator {

    private SimpleSiloCtrl simpleSiloCtrl;
    private ArrayBlockingQueue<SiloCtrlEvent> siloCtrlEventQueue;
    private LiqueurTypeAGenP liqueurTypeA;
    LiqueurPlant plant;
    public LiqueurTypeAGenP lgpA;
    public static Logger LOGGER;

    @Override
    public void start(BundleContext context) throws Exception {
        plant = new LiqueurPlant();
        plant.simpleSiloCtrl = new SimpleSiloCtrl(plant, SimpleSiloCtrlState.EMPTY);
        new Thread(simpleSiloCtrl).start();
        plant.liqueurTypeA = new LiqueurTypeAGenP(plant.siloCtrlEventQueue);
        new Thread(plant.liqueurTypeA).start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        liqueurTypeA = null;
        siloCtrlEventQueue = null;
        simpleSiloCtrl = null;
        plant = null;
    }

    public ArrayBlockingQueue<SiloCtrlEvent> getSiloCtrlEventQueue() {
        return siloCtrlEventQueue;
    }

    public void setSiloCtrlEventQueue(ArrayBlockingQueue<SiloCtrlEvent> siloCtrlEventQueue) {
        this.siloCtrlEventQueue = siloCtrlEventQueue;
    }
}
