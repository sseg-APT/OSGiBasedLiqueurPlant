package liqueurplant.osgi.silo.driver.sim;

import liqueurplant.osgi.silo.controller.api.Process2SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.Driver2SiloCtrlEvent;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bocha on 1/3/2017.
 */


@Component(immediate = true)
public class SimpleSiloDriver implements SiloDriverIf, Runnable {

    private SiloCtrlIf siloCtrl;
    public Logger LOGGER = LoggerFactory.getLogger(SimpleSiloDriver.class);
    private ArrayBlockingQueue<Driver2SiloCtrlEvent> driverEq;
    Driver2SiloCtrlEvent curEvent;


    public SimpleSiloDriver() {
        driverEq = new ArrayBlockingQueue<>(10);
    }

    @Activate
    public void activate() {
        LOGGER.info("SILO-DRIVER activated.");
        new Thread(this).start();
    }

    @Deactivate
    public void deactivate() {
        LOGGER.info("SILO-DRIVER deactivated.");

    }

    @Override
    public void run() {
        curEvent = getNextEvent();
        while(curEvent != null){
            siloCtrl.put2EventQueue(curEvent);
            if(curEvent.equals(Process2SiloCtrlEvent.STOP)) break;
            curEvent = getNextEvent();
            LOGGER.info("Simple Silo Driver :Event arrived=" + curEvent);
        }
        LOGGER.warn("Simple Silo Driver: terminated");

    }

    private Driver2SiloCtrlEvent getNextEvent() {
        // TODO Auto-generated method stub
        Driver2SiloCtrlEvent event = null;

        try {
            event = driverEq.take();
            Thread.sleep(1);
            //LiqueurPlant.LOGGER.severe(event.toString());
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return event;

    }


    @Reference
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOGGER.info(" SILO-CONTROLLER binded.");
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl) {
        this.siloCtrl = null;
        LOGGER.info(" SILO-CONTROLLER unbinded.");
    }



}
