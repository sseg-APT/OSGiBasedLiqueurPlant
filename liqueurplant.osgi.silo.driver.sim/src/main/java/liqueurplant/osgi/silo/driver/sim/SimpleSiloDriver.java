package liqueurplant.osgi.silo.driver.sim;

import liqueurplant.osgi.silo.controller.api.Process2SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.controller.api.SimpleSiloSMEvent;
import liqueurplant.osgi.silo.driver.api.Driver2SiloCtrlEvent;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

@Component(
        name = "liqueurplant.osgi.silo.driver.sim",
        service = liqueurplant.osgi.silo.driver.api.SiloDriverIf.class
)
public class SimpleSiloDriver implements SiloDriverIf, Runnable {

    private SiloCtrlIf siloCtrl;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloDriver.class);
    private ArrayBlockingQueue<Driver2SiloCtrlEvent> driverEq;
    SimpleSiloSMEvent curEvent;

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
            siloCtrl.put2MsgQueue(curEvent);
            if(curEvent.equals(Process2SiloCtrlEvent.STOP)) break;
            curEvent = getNextEvent();
            LOGGER.info("Simple Silo Driver :Event arrived=" + curEvent);
        }
        LOGGER.warn("Simple Silo Driver: terminated");
    }

    private Driver2SiloCtrlEvent getNextEvent() {
        Driver2SiloCtrlEvent event = null;
        try {
            event = driverEq.take();
            Thread.sleep(1);
        } catch (InterruptedException e) {
            LOGGER.error("Exception in getNextEvent(): " + e.toString());
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
