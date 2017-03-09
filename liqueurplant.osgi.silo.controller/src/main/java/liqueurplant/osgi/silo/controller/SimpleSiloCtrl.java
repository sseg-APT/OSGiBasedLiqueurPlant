package liqueurplant.osgi.silo.controller;

//import liqueurplant.osgi.plant.LiqueurPlant;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.ArrayBlockingQueue;

@Component(
        name = "liqueurplant.osgi.silo.controller.SimpleSiloController",
        service = liqueurplant.osgi.silo.controller.api.SiloCtrlIf.class,
        immediate = true
)
public class SimpleSiloCtrl extends Thread implements SiloCtrlIf {

    public ArrayBlockingQueue siloCtrlEventQueue;
    private SiloDriverIf siloDriver;

    public SimpleSiloCtrl() {

    }

    public void run() {

    }

    @Override
    public void fill() throws Exception {
        System.out.println("Silo fill");
        System.out.println(SiloDriverIf.Event.HIGH_LEVEL_REACHED.toString());
    }

    @Override
    public void empty() throws Exception {

    }

    @Override
    public void put2EventQueue(Object event) throws Exception {
        siloCtrlEventQueue.put(event);
    }

    @Reference(
            name ="siloDriver.service",
            service = SiloDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetSiloDriver"
    )
    private void setSiloDriver(SiloDriverIf siloDriver){
        System.out.println("Silo driver binded.");
        this.siloDriver = siloDriver;
    }

    private void unsetSiloDriver(SiloDriverIf siloDriver){
        System.out.println("Silo driver unbinded.");
        this.siloDriver = null;
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
