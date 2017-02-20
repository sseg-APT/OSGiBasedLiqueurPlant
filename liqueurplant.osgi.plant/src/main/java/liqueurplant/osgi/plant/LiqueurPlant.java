package liqueurplant.osgi.plant;

import liqueurplant.osgi.silo.SiloCtrl;
import liqueurplant.osgi.silo.SiloCtrlEvent;
import liqueurplant.osgi.silo.SiloCtrlState;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bocha on 20/2/2017.
 */
public class LiqueurPlant implements BundleActivator {

    private SiloCtrl siloCtrl;
    private ArrayBlockingQueue<SiloCtrlEvent> siloCtrlEventQueue;
    private LiqueurTypeAGen liqueurTypeA;
    LiqueurPlant plant;

    @Override
    public void start(BundleContext context) throws Exception {
        plant = new LiqueurPlant();
        plant.siloCtrl = new SiloCtrl(plant, SiloCtrlState.EMPTY);
        plant.siloCtrl.start();
        plant.liqueurTypeA = new LiqueurTypeAGen(plant.siloCtrlEventQueue);
        new Thread(plant.liqueurTypeA).start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        liqueurTypeA = null;
        siloCtrlEventQueue = null;
        siloCtrl = null;
        plant = null;
    }

    public ArrayBlockingQueue<SiloCtrlEvent> getSiloCtrlEventQueue() {
        return siloCtrlEventQueue;
    }

    public void setSiloCtrlEventQueue(ArrayBlockingQueue<SiloCtrlEvent> siloCtrlEventQueue) {
        this.siloCtrlEventQueue = siloCtrlEventQueue;
    }
}
