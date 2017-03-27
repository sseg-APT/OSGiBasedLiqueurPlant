package liqueurplant.osgi.silo.controller.sim;

import liqueurplant.osgi.silo.controller.api.*;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

@Component(immediate = true)
public class SimpleSiloCtrl implements SiloCtrlIf, Runnable {

    ArrayBlockingQueue<SiloCtrlEvent> eventQueue;
    InValveDriverIf inValve;
    OutValveDriverIf outValve;
    SimpleSiloCtrlState state = SimpleSiloCtrlState.IDLE;
    static Logger LOGGER = LoggerFactory.getLogger(SimpleSiloCtrl.class);
    private ArrayList<NotificationListener> notifications;

    public SimpleSiloCtrl() {
        eventQueue = new ArrayBlockingQueue<>(20);
        notifications = new ArrayList<>(10);
    }

    @Activate
    public void activate() {
        new Thread(this).start();
        LOGGER.info("SILO CONTROLLER activated.");
    }

    @Deactivate
    public void deactivate() {
        LOGGER.info("SILO CONTROLLER deactivated.");
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
    public void run() {
        SiloCtrlEvent scEvent;
        SimpleSiloCtrlState newState;
        boolean stop = false;

        LOGGER.info("CONTROLLER state: " + state + "\n");
        while (state != null) {
            if (stop) {
                stopProcess();
                break;
            }
            scEvent = getNextEvent();
            LOGGER.info("S1: Event arrived = " + scEvent + "\n");
            if (scEvent.equals(Process2SiloCtrlEvent.STOP)) {
                stop = true;
            } else {
                newState = this.state.processEvent(this, scEvent);
                notifyListeners(new ObservableTuple(null, newState));
                if (newState != state) {
                    state = newState;
                    LOGGER.info("S1: Controller State= " + state + "\n");
                    if ((state == SimpleSiloCtrlState.FILLING) || (state == SimpleSiloCtrlState.EMPTYING)) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void stopProcess() {
        LOGGER.warn("S1 Ctrl: stopped");
    }

    private SiloCtrlEvent getNextEvent() {
        SiloCtrlEvent event = null;
        try {
            event = eventQueue.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return event;
    }

    @Override
    public void addListener(NotificationListener listener) {
        notifications.add(listener);
    }

    void notifyListeners(ObservableTuple observable) {
        for (NotificationListener listener : notifications) {
            listener.updateNotification(observable);
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
