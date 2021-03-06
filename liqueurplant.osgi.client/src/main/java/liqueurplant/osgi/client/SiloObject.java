package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.*;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;


public class SiloObject extends BaseInstanceEnabler implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    static int modelId = 20000;
    static SiloCtrlIf siloCtrl;
    private String state = "";
    private boolean fillingCompleted = false;
    private boolean emptyingCompleted = false;
    private String event;

    SiloObject() {

        event = new JSONObject()
            .put("event", "")
            .put("timestamp", "")
            .put("value", "").toString();
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
            case 15:
                return ReadResponse.success(resourceid, event);
            default:
                return super.read(resourceid);
        }
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 1:
                initialize();
                return ExecuteResponse.success();
            case 2:
                LOG.debug("Signal received: " + resourceid);
                return addSignal(params, FillSignal.class);
            case 3:
                LOG.debug("Signal received: " + resourceid);
                return addSignal(params, EmptySignal.class);
            case 4:
                LOG.debug("Signal received: " + resourceid);
                return addSignal(params, HeatSignal.class);
            case 5:
                LOG.debug("Signal received: " + resourceid);
                return addSignal(params, MixSignal.class);
            case 6:
                LOG.debug("Signal received: " + resourceid);
                return addSignal(params, StopFillingSignal.class);
            case 7:
                LOG.debug("Signal received: " + resourceid);
                return addSignal(params, StopEmptyingSignal.class);
            case 8:
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (siloCtrl != null) {
                updateEvent(siloCtrl.takeNotification());
            }
        }
    }

    private <T extends BaseSignal> ExecuteResponse addSignal(String args, Class<T> clazz){

        if (args == null){
            return ExecuteResponse.badRequest("Arguments not correct");
        }
        try {
            Constructor<T> ctor = clazz.getConstructor(String.class);
            BaseSignal sign = ctor.newInstance(args);
            siloCtrl.put2MsgQueue(sign);
        } catch (Exception e){
            return ExecuteResponse.badRequest("Arguments not correct:" + e.getMessage());
        }
        return ExecuteResponse.success();
    }

    private void updateEvent(SMReception newSignal) {
        String newEvent;

        if (newSignal instanceof FillingCompletedSignal){
            newEvent = "FILLING_COMPLETED";
            LOG.info("New event " + newEvent);
        }
        else if (newSignal instanceof EmptyingCompletedSignal){
            newEvent = "EMPTYING_COMPLETED";
            LOG.info("New event " + newEvent);
        }
        else if (newSignal instanceof HeatingCompletedSignal){
            newEvent = "HEATING_COMPLETED";
            LOG.info("New event " + newEvent);
        }
        else if (newSignal instanceof MixingCompletedSignal){
            newEvent = "MIXING_COMPLETED";
            LOG.info("New event " + newEvent);
        }
        else{
            return;
        }

        event = new JSONObject()
            .put("event", newEvent)
            .put("timestamp", (new Timestamp(System.currentTimeMillis())))
            .put("value", "").toString();
        fireResourcesChange(15);

    }


//    public void stop() {
//        gc();
//    }
//
//    public static void gc() {
//        Object obj = new Object();
//        WeakReference ref = new WeakReference<Object>(obj);
//        obj = null;
//        while(ref.get() != null) {
//            System.gc();
//        }
//    }


    private void initialize() {
        setEmptyingCompleted(false);
        setFillingCompleted(false);
    }
//
//
//    private void setState(String newState) {
//        if (!newState.equals(state)) {
//            state = newState;
//            fireResourcesChange(0);
//        }
//    }

    private void setFillingCompleted(Boolean newValue) {
        fillingCompleted = newValue;
        fireResourcesChange(10);
    }

    private void setEmptyingCompleted(Boolean newValue) {
        emptyingCompleted = newValue;
        fireResourcesChange(11);
    }



    protected void setSiloController(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOG.info("SILO CONTROLLER binded.");
    }

    protected void unsetSiloController() {
        this.siloCtrl = null;
        LOG.info("SILO CONTROLLER unbinded.");
    }
}
