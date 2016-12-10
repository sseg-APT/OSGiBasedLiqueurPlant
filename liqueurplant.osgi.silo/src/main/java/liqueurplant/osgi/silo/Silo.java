package liqueurplant.osgi.silo;


import liqueurplant.osgi.silo.api.SiloIf;
import liqueurplant.osgi.valve.Valve;
import liqueurplant.osgi.valve.api.ValveIf;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.osgi.service.component.annotations.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component(name = "liqueurplant.silo")
public class Silo implements SiloIf {

    public String state = "";
    public Boolean emptyingCompleted = new Boolean(true);
    public Boolean fillingCompleted = new Boolean(false);
    public Double targetTemperature = new Double(0.0);
    private ValveIf inValve;
    private ValveIf outValve;


    @Override
    public void fill() {
        try {
            SiloActivator.LOG.debug("Executing fill...");
            setEmptyingCompleted(false);
            inValve.open();
            setState("FILLING");
            Thread.sleep(5000);
            inValve.close();
            SiloActivator.LOG.debug("Fill completed.");
            setFillingCompleted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void empty() {
        try {
            SiloActivator.LOG.debug("Executing empty...");
            setFillingCompleted(false);
            outValve.open();
            setState("EMPTYING");
            Thread.sleep(5000);
            outValve.close();
            SiloActivator.LOG.debug("Empty completed.");
            setEmptyingCompleted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ///*
    public void setState(String newState) {
        this.state = newState;
    }

    public void setFillingCompleted(Boolean newValue) {
        fillingCompleted = newValue;
    }

    public void setEmptyingCompleted(Boolean newValue) {
        emptyingCompleted = newValue;
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

    public void updateInValve(Valve inValve) {
        this.inValve = null;
        this.inValve = inValve;
    }

    public void updateOutValve(Valve outValve) {

        this.outValve = null;
        this.outValve = outValve;
    }
    //*/
}
