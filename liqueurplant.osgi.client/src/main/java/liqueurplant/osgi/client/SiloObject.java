package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.Ctrl2WrapperEvent;
import liqueurplant.osgi.silo.controller.api.ObservableTuple;
import liqueurplant.osgi.silo.controller.api.Process2SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SiloObject extends BaseInstanceEnabler {

    public static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    public static int modelId = 20000;
    static SiloCtrlIf siloCtrl;
    public String state = "";
    public boolean fillingCompleted = false;
    public boolean emptyingCompleted = false;

    public SiloObject(){
        new Thread(() -> {
            ObservableTuple observation;
            while(true){
                if(siloCtrl != null){
                    observation = siloCtrl.getFromStateQueue();
                    LOG.info("Ctrl2Wrapper Event arrived: " + observation.toString());
                    if(observation.getEvent() != null){
                        if(observation.getEvent() == Ctrl2WrapperEvent.FILLING_COMPLETED){
                            setFillingCompleted(true);
                        }
                        else if (observation.getEvent() == Ctrl2WrapperEvent.EMPTYING_COMPLETED){
                            setEmptyingCompleted(true);
                        }
                    }
                    setState(observation.getState().toString());
                }
            }
        }).start();

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
        //*/
        //return super.read(resourceid);
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 1:
                setEmptyingCompleted(false);
                setFillingCompleted(false);
                fill();
                return ExecuteResponse.success();
            case 2:
                setEmptyingCompleted(false);
                setFillingCompleted(false);
                empty();
                return ExecuteResponse.success();
            case 3:
                stop();
                return ExecuteResponse.success();
            case 4:
                setEmptyingCompleted(false);
                setFillingCompleted(false);
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
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.START);
    }

    public void heat() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.START_HEATING);
    }

    public void mix() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.START_MIXING);
    }

    private void setState(String newState) {
        if ( !newState.equals(state)) {
            state = newState;
            fireResourcesChange(0);
        }
    }

    private void setFillingCompleted(Boolean newValue) {
        fillingCompleted = newValue;
        fireResourcesChange(7);
    }

    private void setEmptyingCompleted(Boolean newValue) {
        emptyingCompleted = newValue;
        fireResourcesChange(8);
    }

    private void setTemperature(Double newTemp) {
        //siloComponent.targetTemperature = newTemp;
        fireResourcesChange(11);
    }


    protected void setSiloController(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOG.info("SILO CONTROLLER binded.");
    }

    protected void unsetSiloController(){
        this.siloCtrl = null;
        LOG.info("SILO CONTROLLER unbinded.");
    }

}
