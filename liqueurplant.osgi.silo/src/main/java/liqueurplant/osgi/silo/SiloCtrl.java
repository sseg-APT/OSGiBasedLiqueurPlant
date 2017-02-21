package liqueurplant.osgi.silo;


import liqueurplant.osgi.plant.LiqueurPlant;
import liqueurplant.osgi.silo.api.SiloCtrlIf;
import liqueurplant.osgi.valve.ValveDriver;
import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


@Component(name = "liqueurplant.siloCtrl")
// New bundle Silo driver(controls gpio) ,ValveDriver driver, mixer driver, heater driver(includes thermometer) implements If used by silocontroller
public class SiloCtrl extends Thread implements SiloCtrlIf {

    public String state = "";
    public Boolean emptyingCompleted = new Boolean(true);
    public Boolean fillingCompleted = new Boolean(false);
    public Double targetTemperature = new Double(0.0);
    private ValveIf inValve;
    private ValveIf outValve;
    private List<StateChangedListener> stateChangedListeners = new ArrayList<>();
    private SiloCtrlState siloState;
    private ArrayBlockingQueue<SiloCtrlEvent> eventQueue;
    private LiqueurPlant plant;


    public SiloCtrl(LiqueurPlant plant, SiloCtrlState s) {
        this.plant = plant;
        siloState = s;
        eventQueue = new ArrayBlockingQueue<SiloCtrlEvent>(20);
        plant.setSiloCtrlEventQueue(eventQueue);
        this.setInValve(new ValveDriver("IN", eventQueue));
        this.setOutValve(new ValveDriver("OUT", eventQueue));
    }

    public void run() {
        SiloCtrlEvent siloCtrlEvent;
        SiloCtrlState newSiloCtrlState = null;

        System.out.println("\nCurrent state: " + siloState);
        while (siloState != SiloCtrlState.TERMINATE) {
            siloCtrlEvent = getNextEvent();
            System.out.println("\tEvent arrived: " + siloCtrlEvent);
            newSiloCtrlState = this.siloState.processEvent(this.siloState, siloCtrlEvent);
            if (newSiloCtrlState != siloState) {
                siloState = newSiloCtrlState;
                if (siloCtrlEvent == SiloCtrlEvent.FILL) {
                    this.fill();
                } else if (siloCtrlEvent == SiloCtrlEvent.EMPTY) {
                    this.empty();
                }
                System.out.println("\nCurrent State: " + siloState);

            }
        }
        System.out.println("Silo Controller terminated");
    }

    private SiloCtrlEvent getNextEvent() {
        // TODO Auto-generated method stub
        SiloCtrlEvent event = null;
        try {
            event = eventQueue.take();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return event;

    }

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
