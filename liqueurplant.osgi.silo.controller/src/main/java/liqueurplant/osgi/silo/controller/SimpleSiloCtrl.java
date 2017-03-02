package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.plant.LiqueurPlant;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.SiloDriverEvent;
import liqueurplant.osgi.silo.driver.SimpleSiloDriver;
import liqueurplant.osgi.valve.ValveDriver;
import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Component(name = "liqueurplant.siloCtrl")
public class SimpleSiloCtrl extends SiloCtrl implements Runnable {

    LiqueurPlant itsPlant;
    public SimpleSiloDriver itsDriver;
    SimpleSiloCtrlState state;
    public ArrayBlockingQueue<SiloCtrlEvent> itsEq;
    private ArrayBlockingQueue<SiloDriverEvent> itsDriverEq;

    public SimpleSiloCtrl(LiqueurPlant plant, SimpleSiloCtrlState s) {
        this.itsPlant = plant;
        state = s;
        itsEq = new ArrayBlockingQueue<SiloCtrlEvent>(20);
        itsDriver = new SimpleSiloDriver(plant, itsEq);
        itsDriverEq = itsDriver.itsEq;
    }

    public void run() {
        SiloCtrlEvent scEvent;
        SimpleSiloCtrlState newState = null;
        boolean stop = false;

        new Thread(itsDriver).start();
        LiqueurPlant.LOGGER.info("S1: Controller State = " + state + "\n");
        while (state != null) {
            if (stop) {
                stopProcess();
                break;
            }
            scEvent = getNextEvent();
            LiqueurPlant.LOGGER.fine("S1: Event arrived = " + scEvent + "\n");
            if (scEvent == SiloCtrlEvent.STOP)
                stop = true;
            else {
                newState = this.state.processEvent(this, scEvent);
                if (newState != state) {
                    state = newState;
                    LiqueurPlant.LOGGER.fine("S1: Controller State= " + state + "\n");
                }
            }
        }
    }
    private void stopProcess(){
        try {
            itsDriver.itsEq.put(SiloDriverEvent.STOP);

            LiqueurPlant.LOGGER.severe("S1 Ctrl: stopped");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private SiloCtrlEvent getNextEvent() {
        // TODO Auto-generated method stub
        SiloCtrlEvent event = null;

        try {
            event = itsEq.take();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return event;

    }

    public ArrayBlockingQueue<SiloCtrlEvent> getEventQueue() {
        // TODO Auto-generated method stub
        return this.itsEq;
    }
    /*
    public interface StateChangedListener {
        void stateChanged(String newState);

    }

    @Override
    public void fill() {
        try {
            inValve.open();
            setState("FILLING");
            Thread.sleep(5000);
            inValve.close();
            setState("FULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void empty() {
        try {
            outValve.open();
            setState("EMPTYING");
            Thread.sleep(5000);
            outValve.close();
            setState("EMPTY");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ///*
    public void setState(String newState) {
        this.state = newState;
        for (StateChangedListener listener : stateChangedListeners) {
            listener.stateChanged(newState);
        }
    }

    public void setTemperature(Double newTemp) {
        targetTemperature = newTemp;
    }

    public void setInValve(ValveDriver inValveDriver) {
        this.inValve = inValveDriver;
    }

    public void setOutValve(ValveDriver outValveDriver) {
        this.outValve = outValveDriver;
    }

    public void addStateListener(StateChangedListener listener) {
        stateChangedListeners.add(listener);
    }

    public void updateInValve(ValveDriver inValveDriver) {
        this.inValve = null;
        this.inValve = inValveDriver;
    }

    public void updateOutValve(ValveDriver outValveDriver) {

        this.outValve = null;
        this.outValve = outValveDriver;
    }

    public String getSiloState() {
        return this.state;
    }
    //*/
}
