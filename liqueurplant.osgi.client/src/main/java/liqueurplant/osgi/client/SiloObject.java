package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.*;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;


public class SiloObject extends BaseInstanceEnabler implements NotificationListener, Runnable{

    public static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    public static int modelId = 20000;
    static SiloCtrlIf siloCtrl;
    public String state = "";
    public boolean fillingCompleted = false;
    public boolean emptyingCompleted = false;
    ArrayBlockingQueue<ObservableTuple> observationQueue;

    public SiloObject(){
        observationQueue = new ArrayBlockingQueue<>(20);
    }

    @Override
    public ReadResponse read(int resourceid) {

        switch (resourceid) {
            case 0:
                return ReadResponse.success(resourceid, state);
            case 10:
                return ReadResponse.success(resourceid, fillingCompleted);
            case 11:
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
                initialize();
                return ExecuteResponse.success();
            case 2:
                fill();
                return ExecuteResponse.success();
            case 3:
                empty();
                return ExecuteResponse.success();
            case 6:
                stopFilling();
                return ExecuteResponse.success();
            case 7:
                stopEmptying();
                return ExecuteResponse.success();
            case 8:
                stop();
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }

    @Override
    public void run() {
        ObservableTuple observation;
        LOG.info("Leshan Wrapper started.");
        while(true){
            if(siloCtrl != null){
                try {
                    observation = observationQueue.take();

                    if(observation.getEvent() != null){
                        LOG.info("Ctrl2Wrapper Event arrived: " + observation.getEvent().toString());
                        if(observation.getEvent() == Ctrl2WrapperEvent.FILLING_COMPLETED){
                            setFillingCompleted(true);
                        }
                        else if (observation.getEvent() == Ctrl2WrapperEvent.EMPTYING_COMPLETED){
                            setEmptyingCompleted(true);
                        }
                    }
                    setState(observation.getState().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void fill() {
        setEmptyingCompleted(false);
        setFillingCompleted(false);
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.FILL);
    }

    public void empty() {
        setEmptyingCompleted(false);
        setFillingCompleted(false);
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.EMPTY);
    }

    public void stop() {
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.STOP);
    }

    public void initialize() {
        setEmptyingCompleted(false);
        setFillingCompleted(false);
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.START);
    }


    private void setState(String newState) {
        if ( !newState.equals(state)) {
            state = newState;
            fireResourcesChange(0);
        }
    }

    private void setFillingCompleted(Boolean newValue) {
        fillingCompleted = newValue;
        fireResourcesChange(10);
    }

    private void setEmptyingCompleted(Boolean newValue) {
        emptyingCompleted = newValue;
        fireResourcesChange(11);
    }

    private void stopFilling() {
        LOG.debug("Stop emptying");
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.STOP_FILLING);

    }
    private void stopEmptying() {
        LOG.debug("Stop emptying");
        siloCtrl.put2EventQueue(Process2SiloCtrlEvent.STOP_EPMTYING);

    }

    protected void setSiloController(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        siloCtrl.addListener(this);
        LOG.info("SILO CONTROLLER binded.");
    }

    protected void unsetSiloController(){
        this.siloCtrl = null;
        LOG.info("SILO CONTROLLER unbinded.");
    }

    @Override
    public void updateNotification(ObservableTuple observable) {
        try {
            observationQueue.put(observable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
