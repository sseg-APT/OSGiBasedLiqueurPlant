package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.Process2SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component( immediate = true )
public class SimpleSiloCtrl implements SiloCtrlIf, Runnable {

    protected ArrayBlockingQueue<SiloCtrlEvent> eventQueue;
    protected InValveDriverIf inValve;
    protected OutValveDriverIf outValve;
    SimpleSiloCtrlState state = SimpleSiloCtrlState.EMPTY;
    public static Logger LOGGER = LoggerFactory.getLogger(SimpleSiloCtrl.class);
    public boolean fillingCompleted = false;

    public SimpleSiloCtrl() {
        eventQueue = new ArrayBlockingQueue<>(20);
    }

    @Activate
    public void activate() {
        LOGGER.info("Silo controller activated.");
        new Thread(this).start();
    }

    @Deactivate
    public void deactivate() {

    }

    @Override
    public void put2EventQueue(SiloCtrlEvent event) {
        try {
            eventQueue.put(event);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean getFillingCompleted() {
        return this.fillingCompleted;
    }

    @Override
    public void run() {
        System.out.println("Run controller");
        SiloCtrlEvent scEvent;
        SimpleSiloCtrlState newState = null;

        boolean stop = false;
        LOGGER.info("CONTROLLER state: " + state + "\n");
        while(state != null){
            if(stop){
                stopProcess();
                break;
            }
            scEvent = getNextEvent();
            LOGGER.info("S1: Event arrived = " + scEvent +"\n");
            if( scEvent.equals(Process2SiloCtrlEvent.STOP)) {
                stop = true;
            } else{
                newState = this.state.processEvent(this, scEvent);
                if(newState != state){
                    state = newState;
                    LOGGER.info("S1: Controller State= " + state +"\n");
                }
            }
        }
    }

    private void stopProcess(){
        LOGGER.warn("S1 Ctrl: stopped");
    }

    private SiloCtrlEvent getNextEvent() {
        SiloCtrlEvent event = null;

        try {
            event = eventQueue.take();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return event;

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
