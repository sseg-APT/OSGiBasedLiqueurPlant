package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.*;
import liqueurplant.osgi.silo.controller.state.machine.SMReception;
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

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

@Component(
        name = "liqueurplant.osgi.silo.controller",
        service = liqueurplant.osgi.silo.controller.api.SiloCtrlIf.class
)
public class SimpleSiloCtrl extends StateMachine implements SiloCtrlIf, Runnable {

    ArrayBlockingQueue<ObservableTuple> notificationQueue;
    ArrayBlockingQueue<SiloCtrlEvent> eventQueue;
    InValveDriverIf inValve;
    OutValveDriverIf outValve;
    private SimpleSiloCtrlState state = SimpleSiloCtrlState.IDLE;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloCtrl.class);

    State empty, filling, full, emptying;
    Transition e2ft, f2ft, f2et,e2et;

    public SimpleSiloCtrl() {
        super(null);
        //eventQueue = new ArrayBlockingQueue<>(20);
        //notificationQueue = new ArrayBlockingQueue<>(20);
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
    public void activate() {
        new Thread(this).start();
        LOGGER.info("SILO CONTROLLER activated.");
    }

    @Deactivate
    public void deactivate() {
        eventQueue = null;
        notificationQueue = null;
        LOGGER.info("SILO CONTROLLER deactivated.");
    }

    @Override
    public void put2MsgQueue(SiloCtrlEvent event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            LOGGER.error("Exception in put2MsgQueue(): " + e.toString());
        }
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

    @Override
    public void run() {
        SiloCtrlEvent scEvent;
        SimpleSiloCtrlState newState;
        boolean stop = false;

        LOGGER.info("CONTROLLER state: " + state + "\n");
        while (state != null) {
            if (stop) {
                stopProcess();
                break;
            }
            scEvent = getNextEvent();
            LOGGER.info("S1: Event arrived = " + scEvent + "\n");
            if (scEvent.equals(Process2SiloCtrlEvent.STOP)) {
                stop = true;
            } else {
                newState = this.state.processEvent(this, scEvent);
                try {
                    notificationQueue.put(new ObservableTuple(null, newState));
                    if (newState != state) {
                        state = newState;
                        LOGGER.info("S1: Controller State= " + state + "\n");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stopProcess() {
        LOGGER.warn("S1 Ctrl: stopped");
    }

    private SiloCtrlEvent getNextEvent() {
        SiloCtrlEvent event = null;
        try {
            event = eventQueue.take();
        } catch (InterruptedException ex) {
            LOGGER.error("Exception in getNextEvent(): " + ex.toString());
        }
        return event;
    }

    private class Empty extends State {
        @Override
        protected void entry() {}
        @Override
        protected void doActivity() {
            System.out.println("Empty doActivity");
        }
        @Override
        protected void exit() {	}
    }

    private class Filling extends State {
        @Override
        protected void entry() {}
        @Override
        protected void doActivity() {
            System.out.println("Filling doActivity");
        }
        @Override
        protected void exit() {	}
    }

    private class Full extends State {
        @Override
        protected void entry() {}
        @Override
        protected void doActivity() {
            System.out.println("Full doActivity");
        }
        @Override
        protected void exit() {	}
    }

    private class Emptying extends State {
        @Override
        protected void entry() {}
        @Override
        protected void doActivity() {
            System.out.println("Emptying doActivity");
        }
        @Override
        protected void exit() {	}
    }

    // transition definitions
    private class Empty2FillingTrans extends Transition {
        public Empty2FillingTrans(State targetState) {
            super(targetState,false,false);
        }
        @Override
        protected boolean trigger(SMReception smr) {
            return (smr == SimpleSiloSMEvent.FILL);
        }

        @Override
        protected void effect() {
            System.out.println("sends open TO inValve");
        }
    }

    private class Filling2FullTrans extends Transition {
        public Filling2FullTrans(State targetState) {
            super(targetState,false,false);
        }
        @Override
        protected boolean trigger(SMReception smr) {
            return ((smr == SimpleSiloSMEvent.HIGH_LEVEL_REACHED) || (smr == SimpleSiloSMEvent.STOP_FILLING));
        }
        @Override
        protected void effect() {
            System.out.println("sends close TO inValve");
        }
    }

    private class Full2EmptyingTrans extends Transition {
        public Full2EmptyingTrans(State targetState) {
            super(targetState,false,false);
        }
        @Override
        protected boolean trigger(SMReception smr) {
            return (smr == SimpleSiloSMEvent.EMPTY);
        }
        @Override
        protected void effect() {
            System.out.println("sends open TO outValve");
        }
    }

    private class Emptying2EmptyTrans extends Transition {
        public Emptying2EmptyTrans(State targetState) {
            super(targetState,false,false);
        }
        @Override
        protected boolean trigger(SMReception smr) {
            return ((smr == SimpleSiloSMEvent.LOW_LEVEL_REACHED) || (smr == SimpleSiloSMEvent.STOP_EMPTYING));
        }
        @Override
        protected void effect() {
            System.out.println("sends close TO outValve");
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
