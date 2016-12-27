package liqueurplant.osgi.silo;


import liqueurplant.osgi.silo.api.SiloCtrlIf;
import liqueurplant.osgi.valve.Valve;
import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;


@Component(name = "liqueurplant.siloCtrl")
// New bundle Silo driver ,Valve driver, mixer driver, heater driver(includes thermometer) implements If used by silocontroller
public class SiloCtrl implements SiloCtrlIf {

    public String state = "";
    public Boolean emptyingCompleted = new Boolean(true);
    public Boolean fillingCompleted = new Boolean(false);
    public Double targetTemperature = new Double(0.0);
    private ValveIf inValve;
    private ValveIf outValve;
    private List<StateChangedListener> stateChangedListeners = new ArrayList<>();

    public interface StateChangedListener {
        void stateChanged(String newState);

    }

    @Override
    public void fill() {
        try {
            SiloActivator.LOG.debug("Executing fill...");
            inValve.open();
            setState("FILLING");
            Thread.sleep(5000);
            inValve.close();
            setState("FULL");
            SiloActivator.LOG.debug("Fill completed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void empty() {
        try {
            SiloActivator.LOG.debug("Executing empty...");
            outValve.open();
            setState("EMPTYING");
            Thread.sleep(5000);
            outValve.close();
            setState("EMPTY");
            SiloActivator.LOG.debug("Empty completed.");
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

    public void setInValve(Valve inValve) {
        this.inValve = inValve;
    }

    public void setOutValve(Valve outValve) {
        this.outValve = outValve;
    }

    public void addStateListener(StateChangedListener listener) {
        stateChangedListeners.add(listener);
    }

    public void updateInValve(Valve inValve) {
        this.inValve = null;
        this.inValve = inValve;
    }

    public void updateOutValve(Valve outValve) {

        this.outValve = null;
        this.outValve = outValve;
    }

    public String getState(){
        return this.state;
    }
    //*/
}
