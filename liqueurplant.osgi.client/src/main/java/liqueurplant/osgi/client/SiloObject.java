package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.SiloCtrl;
import liqueurplant.osgi.valve.Valve;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SiloObject extends BaseInstanceEnabler {

    public static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    public static int modelId = 20000;
    private ExecutorService pool;
    private SiloCtrl siloComponent;

    SiloObject() {
        pool = Executors.newFixedThreadPool(2);
        siloComponent = new SiloCtrl();
        siloComponent.setInValve(new Valve("IN"));
        siloComponent.setOutValve(new Valve("OUT"));
        siloComponent.addStateListener((String newState) -> this.pool.execute(()->setState(newState)));

    }

    @Override
    public ReadResponse read(int resourceid) {
        switch (resourceid) {
            case 0:
                return ReadResponse.success(resourceid, siloComponent.state);
            case 7:
                return ReadResponse.success(resourceid, siloComponent.fillingCompleted);
            case 8:
                return ReadResponse.success(resourceid, siloComponent.emptyingCompleted);
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


    public void fill() {
        setEmptyingCompleted(false);
        siloComponent.fill();
        setFillingCompleted(true);
    }

    public void empty() {
        setFillingCompleted(false);
        siloComponent.empty();
        setEmptyingCompleted(true);
    }

    private void setState(String newState) {
        siloComponent.state = newState;
        fireResourcesChange(0);
    }

    private void setFillingCompleted(Boolean newValue) {
        siloComponent.fillingCompleted = newValue;
        fireResourcesChange(7);
        if (newValue) setState("FULL");
    }

    private void setEmptyingCompleted(Boolean newValue) {
        siloComponent.emptyingCompleted = newValue;
        fireResourcesChange(8);
        if (newValue) setState("EMPTY");
    }

    private void setTemperature(Double newTemp) {
        siloComponent.targetTemperature = newTemp;
        fireResourcesChange(11);
    }
}
