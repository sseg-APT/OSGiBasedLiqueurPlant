package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.plant.LiqueurPlant;
import liqueurplant.osgi.silo.controller.SiloCtrlEvent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bocha on 1/3/2017.
 */
public class SimpleSiloDriver extends SiloDriver implements Runnable {

    public SimpleSiloDriver(LiqueurPlant plant, ArrayBlockingQueue<SiloCtrlEvent> eq) {
        this.plant = plant;
        itsCtrlEq = eq;
        itsEq = new ArrayBlockingQueue<SiloDriverEvent>(10);
    }

    public void run() {
        curEvent = getNextEvent();
        while(curEvent != null) {
            curEvent.sendEvent(this);
            if (curEvent == SiloDriverEvent.STOP) break;
            curEvent = getNextEvent();
            LiqueurPlant.LOGGER.finest("Simple Silo Driver: Event arrived:= " + curEvent);
        }
        LiqueurPlant.LOGGER.warning("Simple Silo Driver: terminated.");

    }

    private SiloDriverEvent getNextEvent() {
        // TODO Auto-generated method stub
        SiloDriverEvent event = null;

        try {
            event = itsEq.take();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return event;

    }

}
