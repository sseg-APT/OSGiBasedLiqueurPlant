package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component( immediate = true )
public class SimpleSiloCtrl implements SiloCtrlIf, Runnable {

    protected ArrayBlockingQueue eventQueue;
    protected InValveDriverIf inValve;
    protected OutValveDriverIf outValve;
    public static Logger LOGGER = LoggerFactory.getLogger(SimpleSiloCtrl.class);

    public SimpleSiloCtrl() {
        eventQueue = new ArrayBlockingQueue<>(20);
    }

    @Activate
    public void activate() {

    }

    @Override
    public void put2EventQueue(Object o) {
        try {
            eventQueue.put(o);
            System.out.println(eventQueue.take().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            if(!eventQueue.isEmpty()){
                try {
                    System.out.println(eventQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Reference
    protected void setInValve(InValveDriverIf inValve) {
        this.inValve = inValve;
        LOGGER.info("IN-VALVE binded.");
    }

    protected void unsetInValve(InValveDriverIf inValve) {
        this.inValve = null;
        LOGGER.info("IN-VALVE unbinded.");
    }

    @Reference
    protected void setOutValve(OutValveDriverIf outValve) {
        this.outValve = outValve;
        LOGGER.info("OUT-VALVE binded.");
    }

    protected void unsetOutValve(OutValveDriverIf outValve) {
        this.outValve = null;
        LOGGER.info("OUT-VALVE unbinded.");
    }

}
