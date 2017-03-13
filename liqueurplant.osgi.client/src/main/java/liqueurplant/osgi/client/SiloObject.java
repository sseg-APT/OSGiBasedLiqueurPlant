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


public class SiloObject extends BaseInstanceEnabler {

    public static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    public static int modelId = 20000;
    static SiloCtrlIf siloCtrl;


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
                empty();
                return ExecuteResponse.success();
            case 3:
                stop();
                return ExecuteResponse.success();
            case 4:
                initialize();
                return ExecuteResponse.success();
            case 5:
                heat();
                return ExecuteResponse.success();
            case 6:
                mix();
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }


    public void fill() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.FILL);
    }

    public void empty() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.EMPTY);
    }

    public void stop() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.STOP);
    }

    public void initialize() {
        //siloCtrl.put2EventQueue(Process2SiloCtrlEvent);
    }

    public void heat() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.START_HEATING);
    }

    public void mix() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.START_MIXING);
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

    @Reference
    protected void setSiloController(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOG.info("SILO CONTROLLER binded.");
    }

    protected void unsetSiloController(){
        this.siloCtrl = null;
        LOG.info("SILO CONTROLLER unbinded.");
    }

}
