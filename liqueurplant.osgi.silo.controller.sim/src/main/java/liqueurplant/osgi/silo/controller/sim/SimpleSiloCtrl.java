package liqueurplant.osgi.silo.controller.sim;

import liqueurplant.osgi.silo.controller.api.*;

import liqueurplant.osgi.silo.controller.state.machine.State;
import liqueurplant.osgi.silo.controller.state.machine.StateMachine;
import liqueurplant.osgi.silo.controller.state.machine.Transition;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

@Component(
        name = "liqueurplant.osgi.silo.controller",
        service = SiloCtrlIf.class
)
public class SimpleSiloCtrl extends StateMachine implements SiloCtrlIf {

    private InValveDriverIf inValve;
    private OutValveDriverIf outValve;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloCtrl.class);

    Transition e2ft, f2ft, f2et, e2et;
    State empty, filling, full, emptying;
    ArrayBlockingQueue<ObservableTuple> notificationQueue;

    public SimpleSiloCtrl() {
        super(null);
        full = new Full();
        empty = new Empty();
        filling = new Filling();
        emptying = new Emptying();
        f2ft = new Filling2FullTrans(full);
        e2et = new Emptying2EmptyTrans(empty);
        e2ft = new Empty2FillingTrans(filling);
        f2et = new Full2EmptyingTrans(emptying);
        full.addTransition(f2et);
        empty.addTransition(e2ft);
        filling.addTransition(f2ft);
        emptying.addTransition(e2et);
        this.setInitState(empty);
        notificationQueue = new ArrayBlockingQueue<>(20);
    }

    @Activate
    public void activate() throws InterruptedException {
        Thread smt = new Thread(this);
        smt.setName("SILO-CTRL");
        smt.start();
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
            LOGGER.debug("Smart Silo state: FILLING");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            LOGGER.debug("Smart Silo state: EMPTYING");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                put2MsgQueue(SimpleSiloSMEvent.HIGH_LEVEL_REACHED);
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
                put2MsgQueue(SimpleSiloSMEvent.LOW_LEVEL_REACHED);
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
            } catch (Exception e) {
                LOGGER.error("Exception in close OUT-VALVE: " + e.toString());
            }
        }
    }


    @Reference
    protected void setInValve(InValveDriverIf inValve) {
        this.inValve = inValve;
        LOGGER.info("IN-VALVE binded.");
    }

    protected void unsetInValve(InValveDriverIf inValve) {
        this.inValve = null;
        LOGGER.info("IN-VALVE unbinded.");
    }

    @Reference
    protected void setOutValve(OutValveDriverIf outValve) {
        this.outValve = outValve;
        LOGGER.info("OUT-VALVE binded.");
    }

    protected void unsetOutValve(OutValveDriverIf outValve) {
        this.outValve = null;
        LOGGER.info("OUT-VALVE unbinded.");
    }
}
