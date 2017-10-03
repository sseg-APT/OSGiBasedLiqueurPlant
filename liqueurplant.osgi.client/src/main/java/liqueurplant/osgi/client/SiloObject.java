package liqueurplant.osgi.client;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import liqueurplant.osgi.silo.controller.api.*;
import liqueurplant.osgi.silo.controller.api.signals.*;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;


public class SiloObject extends BaseInstanceEnabler implements Runnable {

    public static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    public static int modelId = 20000;
    static SiloCtrlIf siloCtrl;
    public String state = "";
    public boolean fillingCompleted = false;
    public boolean emptyingCompleted = false;
    private String event;

    public SiloObject() {

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
                return addSignal(params, FillSignal.class);
            case 3:
                return addSignal(params, EmptySignal.class);
            //case 4:
            //    return addSignal(params, StartHeatingSignal.class);
            case 5:
                return addSignal(params, MixSignal.class);
            case 6:
                return addSignal(params, StopFillingSignal.class);
            case 7:
                return addSignal(params, StopEmptyingSignal.class);
            case 8:
                stop();
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }

    @Override
    public void run() {
        BaseSignal observation;
        LOG.info("Leshan Wrapper started.");
        while (true) {
            if (siloCtrl != null) {
                observation = siloCtrl.takeNotification();
                updateEvent(observation);
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
        LOG.info("New event " + newSignal);

        String newEvent;

        if (newSignal instanceof FillingCompletedSignal){
            newEvent = "FILLING_COMPLETED";
        }
        else if (newSignal instanceof EmptyingCompletedSignal){
            newEvent = "EMPTYING_COMPLETED";
        }
        else if (newSignal instanceof MixingCompletedSignal){
            newEvent = "MIXING_COMPLETED";
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


    public void stop() {
        //siloCtrl.put2MsgQueue();
        gc();
    }

    public static void gc() {
        Object obj = new Object();
        WeakReference ref = new WeakReference<Object>(obj);
        obj = null;
        while(ref.get() != null) {
            System.gc();
        }
    }


    public void initialize() {
        setEmptyingCompleted(false);
        setFillingCompleted(false);
        //siloCtrl.put2MsgQueue(SimpleSiloSMEvent.START);
    }


    private void setState(String newState) {
        if (!newState.equals(state)) {
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



    protected void setSiloController(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOG.info("SILO CONTROLLER binded.");
    }

    protected void unsetSiloController() {
        this.siloCtrl = null;
        LOG.info("SILO CONTROLLER unbinded.");
    }
}
