package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.*;

import liqueurplant.osgi.silo.controller.state.machine.State;
import liqueurplant.osgi.silo.controller.state.machine.StateMachine;
import liqueurplant.osgi.silo.controller.state.machine.Transition;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

@Component(
        name = "liqueurplant.osgi.silo.controller",
        service = SiloCtrlIf.class
)
public class SimpleSiloCtrl extends StateMachine implements SiloCtrlIf {

    ArrayBlockingQueue<ObservableTuple> notificationQueue;
    private InValveDriverIf inValve;
    private OutValveDriverIf outValve;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloCtrl.class);

    State empty, filling, full, emptying;
    Transition e2ft, f2ft, f2et, e2et;

    public SimpleSiloCtrl() {
        super(null);
        notificationQueue = new ArrayBlockingQueue<>(20);
        empty = new Empty();
        filling = new Filling();
        full = new Full();
        emptying = new Emptying();
        e2ft = new Empty2FillingTrans(filling);
        f2ft = new Filling2FullTrans(full);
        f2et = new Full2EmptyingTrans(emptying);
        e2et = new Emptying2EmptyTrans(empty);
        empty.addTransition(e2ft);
        filling.addTransition(f2ft);
        full.addTransition(f2et);
        emptying.addTransition(e2et);
        this.setInitState(empty);
    }

    @Activate
    public void activate() throws InterruptedException {
        Thread smt = new Thread(this);
        smt.setName("SILO-CTRL");
        smt.start();
        notificationQueue.put(new ObservableTuple(null, SiloCtrlState.EMPTY));
        LOGGER.info("SILO CONTROLLER activated.");
    }

    @Deactivate
    public void deactivate() {
        notificationQueue = null;
        LOGGER.info("SILO CONTROLLER deactivated.");
    }


    @Override
    public void put2MsgQueue(SimpleSiloSMEvent event) {
        this.itsMsgQ.add(event);
    }

    @Override
    public ObservableTuple takeNotification() {
        try {
            return notificationQueue.take();
        } catch (InterruptedException e) {
            LOGGER.error("Exception in takeNotification(): " + e.toString());
            return null;
        }
    }

    private class Empty extends State {
        @Override
        protected void entry() {
        }

        @Override
        protected void doActivity() {
            LOGGER.debug("Smart Silo state: EMPTY");
        }

        @Override
        protected void exit() {
        }
    }

    private class Filling extends State {
        @Override
        protected void entry() {

        }

        @Override
        protected void doActivity() {
            try {
                notificationQueue.put(new ObservableTuple(null, SiloCtrlState.FILLING));
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException in Filling.entry(): " + e.toString());
            }
            LOGGER.debug("Smart Silo state: FILLING");
        }

        @Override
        protected void exit() {
        }
    }

    private class Full extends State {
        @Override
        protected void entry() {
        }

        @Override
        protected void doActivity() {
            LOGGER.debug("Smart Silo state: FULL");
        }

        @Override
        protected void exit() {
        }
    }

    private class Emptying extends State {
        @Override
        protected void entry() {

        }

        @Override
        protected void doActivity() {
            try {
                notificationQueue.put(new ObservableTuple(null, SiloCtrlState.EMPTYING));
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException in Emptying.entry(): " + e.toString());
            }
            LOGGER.debug("Smart Silo state: EMPTYING");
        }

        @Override
        protected void exit() {
        }
    }

    // transition definitions
    private class Empty2FillingTrans extends Transition {
        public Empty2FillingTrans(State targetState) {
            super(targetState, false, false);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr == SimpleSiloSMEvent.FILL);
        }

        @Override
        protected void effect() {
            try {
                inValve.open();
            } catch (Exception e) {
                LOGGER.error("Exception in open IN-VALVE: " + e.toString());
            }
        }
    }

    private class Filling2FullTrans extends Transition {
        public Filling2FullTrans(State targetState) {
            super(targetState, false, false);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return ((smr == SimpleSiloSMEvent.HIGH_LEVEL_REACHED) || (smr == SimpleSiloSMEvent.STOP_FILLING));
        }

        @Override
        protected void effect() {
            try {
                inValve.close();
                notificationQueue.put(new ObservableTuple(Ctrl2WrapperEvent.FILLING_COMPLETED, SiloCtrlState.FULL));
            } catch (Exception e) {
                LOGGER.error("Exception in close IN-VALVE: " + e.toString());
            }
        }
    }

    private class Full2EmptyingTrans extends Transition {
        public Full2EmptyingTrans(State targetState) {
            super(targetState, false, false);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr == SimpleSiloSMEvent.EMPTY);
        }

        @Override
        protected void effect() {
            try {
                outValve.open();
            } catch (Exception e) {
                LOGGER.error("Exception in open OUT-VALVE: " + e.toString());
            }
        }
    }

    private class Emptying2EmptyTrans extends Transition {
        public Emptying2EmptyTrans(State targetState) {
            super(targetState, false, false);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return ((smr == SimpleSiloSMEvent.LOW_LEVEL_REACHED) || (smr == SimpleSiloSMEvent.STOP_EMPTYING));
        }

        @Override
        protected void effect() {
            try {
                outValve.close();
                notificationQueue.put(new ObservableTuple(Ctrl2WrapperEvent.EMPTYING_COMPLETED, SiloCtrlState.EMPTY));
            } catch (Exception e) {
                LOGGER.error("Exception in close OUT-VALVE: " + e.toString());
            }
        }
    }


    @Reference(
            policy = ReferencePolicy.DYNAMIC
    )
    protected void setInValve(InValveDriverIf inValve) {
        this.inValve = inValve;
        LOGGER.info("IN-VALVE binded.");
    }

    protected void unsetInValve(InValveDriverIf inValve) {
        this.inValve = null;
        LOGGER.info("IN-VALVE unbinded.");
    }

    @Reference(
            policy = ReferencePolicy.DYNAMIC
    )
    protected void setOutValve(OutValveDriverIf outValve) {
        this.outValve = outValve;
        LOGGER.info("OUT-VALVE binded.");
    }

    protected void unsetOutValve(OutValveDriverIf outValve) {
        this.outValve = null;
        LOGGER.info("OUT-VALVE unbinded.");
    }
}
