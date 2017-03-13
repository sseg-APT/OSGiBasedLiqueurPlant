package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf.Process2SiloCtrlEvent;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component(immediate = true)
public class SiloObject extends BaseInstanceEnabler {

    public static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    public static int modelId = 20000;
    private ExecutorService pool;
    static SiloCtrlIf siloCtrl;

    public SiloObject() {
        pool = Executors.newFixedThreadPool(2);

    }

    @Activate
    public void activate() {
        LOG.info("Client started.");
    }

    @Deactivate
    public void deactivate() {

    }

    @Override
    public ReadResponse read(int resourceid) {
        /*
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
        //*/
        return super.read(resourceid);
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 1:
                fill();
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
        //siloComponent.fill();
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.FILL);
        setFillingCompleted(true);
    }

    public void empty() {
        setFillingCompleted(false);
        //siloComponent.empty();
        setEmptyingCompleted(true);
    }

    private void setState(String newState) {
        //siloComponent.state = newState;
        fireResourcesChange(0);
    }

    private void setFillingCompleted(Boolean newValue) {
        //siloComponent.fillingCompleted = newValue;
        fireResourcesChange(7);
        if (newValue) setState("FULL");
    }

    private void setEmptyingCompleted(Boolean newValue) {
        //siloComponent.emptyingCompleted = newValue;
        fireResourcesChange(8);
        if (newValue) setState("EMPTY");
    }

    private void setTemperature(Double newTemp) {
        //siloComponent.targetTemperature = newTemp;
        fireResourcesChange(11);
    }

    ///*
    @Reference
    protected void setSiloController(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOG.info("SILO CONTROLLER binded.");
    }

    protected void unsetSiloController(SiloCtrlIf siloCtrl){
        this.siloCtrl = null;
        LOG.info("SILO CONTROLLER unbinded.");
    }
    //*/
}
