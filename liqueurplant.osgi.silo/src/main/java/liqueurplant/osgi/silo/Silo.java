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
public class Silo extends BaseInstanceEnabler implements SiloIf {

    public static int modelId = 16663;
    private ExecutorService pool;
    private String state = "";
    private Boolean emptyingCompleted = new Boolean(true);
    private Boolean fillingCompleted = new Boolean(false);
    private Double targetTemperature = new Double(0.0);
    private ValveIf inValve;
    private ValveIf outValve;

    Silo() {
        pool = Executors.newFixedThreadPool(2);
    }

    @Override
    public ReadResponse read(int resourceid) {
        switch (resourceid) {
            case 0:
                return ReadResponse.success(resourceid, state);
            case 7:
                return ReadResponse.success(resourceid, fillingCompleted);
            case 8:
                return ReadResponse.success(resourceid, emptyingCompleted);
            default:
                return super.read(resourceid);
        }
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 1:
                pool.submit(this::fill);
                return ExecuteResponse.success();
            case 2:
                pool.submit(this::empty);
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }


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
    private void setState(String newState) {
        this.state = newState;
        fireResourcesChange(0);
    }

    private void setFillingCompleted(Boolean newValue) {
        fillingCompleted = newValue;
        fireResourcesChange(7);
        if (newValue) setState("FULL");
    }

    private void setEmptyingCompleted(Boolean newValue) {
        emptyingCompleted = newValue;
        fireResourcesChange(8);
        if (newValue) setState("EMPTY");
    }

    private void setTemperature(Double newTemp) {
        targetTemperature = newTemp;
        fireResourcesChange(11);
    }

    protected void setInValve(Valve inValve) {
        this.inValve = inValve;
    }

    protected void setOutValve(Valve outValve) {
        this.outValve = outValve;
    }
    //*/
}
