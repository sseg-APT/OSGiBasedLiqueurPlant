package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.heater.api.HeaterDriverIf;
import liqueurplant.osgi.heater.api.HeatingCompletedListenerIf;
import liqueurplant.osgi.mixer.api.MixerDriverIf;
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
        immediate = true,
        service = SiloCtrlIf.class
)
public class HeatMixSiloCtrl extends StateMachine implements SiloCtrlIf, HeatingCompletedListenerIf {

    ArrayBlockingQueue<BaseSignal> notificationQueue;
    private InValveDriverIf inValve;
    private OutValveDriverIf outValve;
    private MixerDriverIf mixerDriver;
    private HeaterDriverIf heaterDriver;
    private Logger LOGGER = LoggerFactory.getLogger(HeatMixSiloCtrl.class);

    State empty, filling, full, emptying, mixing, mixed, heating, heated;
    Transition e2ft, f2ft, f2et, e2et, m2mt, m2et, f2ht, h2ht, h2mt;

    public HeatMixSiloCtrl() {
        super(null);
        notificationQueue = new ArrayBlockingQueue<>(20);
        empty = new Empty();
        filling = new Filling();
        full = new Full();
        emptying = new Emptying();
        mixing = new Mixing();
        mixed = new Mixed();
        heating = new Heating();
        heated = new Heated();
        e2ft = new Empty2FillingTrans(empty, filling);
        f2ft = new Filling2FullTrans(filling, full);
        f2et = new Full2EmptyingTrans(full, emptying);
        e2et = new Emptying2EmptyTrans(emptying, empty);
        f2ht = new Full2HeatingTrans(full, heating);
        h2ht = new Heating2HeatedTrans(heating, heated);
        h2mt = new Heated2MixingTrans(heated, mixing);
        m2mt = new Mixing2MixedTrans(mixing, mixed);
        m2et = new Mixed2EmptyingTrans(mixed, emptying);

        this.setInitState(empty);
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
    public void put2MsgQueue(BaseSignal signal) {
        this.itsMsgQ.add(signal);
        LOGGER.info("Signal: " + signal.toString());
    }

    @Override
    public BaseSignal takeNotification() {
        try {
            return notificationQueue.take();
        } catch (InterruptedException e) {
            LOGGER.error("Exception in takeNotification(): " + e.toString());
            return null;
        }
    }

    @Override
    public void heatingCompleted() {
        put2MsgQueue(new HeatingCompletedSignal());
    }

    private class Empty extends State {
        @Override
        protected void entry() {
            LOGGER.debug("Smart Silo state: EMPTY");
        }

        @Override
        protected void doActivity() {
        }

        @Override
        protected void exit() {

        }
    }

    private class Filling extends State {
        @Override
        protected void entry() {
            try {
                inValve.open();
            } catch (Exception e) {
                LOGGER.error("Exception in open IN-VALVE: " + e.toString());
            }
            LOGGER.debug("Smart Silo state: FILLING");
        }

        @Override
        protected void doActivity() {

        }

        @Override
        protected void exit() {
            try {
                inValve.close();
                notificationQueue.put(new FillingCompletedSignal());
            } catch (Exception e) {
                LOGGER.error("Exception in close IN-VALVE: " + e.toString());
            }
        }
    }

    private class Full extends State {
        @Override
        protected void entry() {
            LOGGER.debug("Smart Silo state: FULL");
        }

        @Override
        protected void doActivity() {
        }

        @Override
        protected void exit() {
        }
    }

    private class Emptying extends State {
        @Override
        protected void entry() {
            try {
                outValve.open();
            } catch (Exception e) {
                LOGGER.error("Exception in open OUT-VALVE: " + e.toString());
            }
            LOGGER.debug("Smart Silo state: EMPTYING");
        }

        @Override
        protected void doActivity() {

        }

        @Override
        protected void exit() {
            try {
                outValve.close();
                notificationQueue.put(new EmptyingCompletedSignal());
            } catch (Exception e) {
                LOGGER.error("Exception in close OUT-VALVE: " + e.toString());
            }
        }
    }

    private class Mixing extends State {
        @Override
        protected void entry() {
            mixerDriver.start();
            LOGGER.debug("Smart Silo state: MIXING");
        }

        @Override
        protected void doActivity() {
            try {
                Thread.sleep(4000);
                put2MsgQueue(new MixingCompletedSignal());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void exit() {
            mixerDriver.stop();
            try {
                notificationQueue.put(new MixingCompletedSignal());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Mixed extends State {
        @Override
        protected void entry() {
            LOGGER.debug("Smart Silo state: MIXED");
        }

        @Override
        protected void doActivity() {
        }

        @Override
        protected void exit() {
        }
    }

    private class Heating extends State {
        @Override
        protected void entry() {
            heaterDriver.heat2temp(35.0f);
            LOGGER.debug("Smart Silo state: HEATING");
        }

        @Override
        protected void doActivity() {

        }

        @Override
        protected void exit() {
            try {
                notificationQueue.put(new HeatingCompletedSignal());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private class Heated extends State {
        @Override
        protected void entry() {
            LOGGER.debug("Smart Silo state: HEATED");
        }

        @Override
        protected void doActivity() {

        }

        @Override
        protected void exit() {
        }
    }

    // transition definitions
    private class Empty2FillingTrans extends Transition {

        public Empty2FillingTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr instanceof FillSignal);
        }

        @Override
        protected void effect() {

        }
    }

    private class Filling2FullTrans extends Transition {

        public Filling2FullTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return ((smr instanceof HighLevelReachedSignal) || (smr instanceof StopFillingSignal));
        }

        @Override
        protected void effect() {

        }
    }

    private class Full2EmptyingTrans extends Transition {

        public Full2EmptyingTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr instanceof EmptySignal);
        }

        @Override
        protected void effect() {

        }
    }

    private class Emptying2EmptyTrans extends Transition {

        public Emptying2EmptyTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return ((smr instanceof LowLevelReachedSignal) || (smr instanceof StopEmptyingSignal));
        }

        @Override
        protected void effect() {

        }
    }

    private class Full2HeatingTrans extends Transition {

        public Full2HeatingTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr instanceof HeatSignal);
        }

        @Override
        protected void effect() {

        }
    }

    private class Heating2HeatedTrans extends Transition {

        public Heating2HeatedTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr instanceof HeatingCompletedSignal);
        }

        @Override
        protected void effect() {

        }
    }

    private class Heated2MixingTrans extends Transition {

        public Heated2MixingTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            LOGGER.info("MIXSignal");
            return (smr instanceof MixSignal);
        }

        @Override
        protected void effect() {

        }
    }

    private class Mixing2MixedTrans extends Transition {

        public Mixing2MixedTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr instanceof MixingCompletedSignal);
        }

        @Override
        protected void effect() {

        }
    }

    private class Mixed2EmptyingTrans extends Transition {

        public Mixed2EmptyingTrans(State fromState, State toState) {
            super(fromState, toState);
        }

        @Override
        protected boolean trigger(SMReception smr) {
            return (smr instanceof EmptySignal);
        }

        @Override
        protected void effect() {

        }
    }


    @Reference(
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.OPTIONAL
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
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.OPTIONAL
    )
    protected void setOutValve(OutValveDriverIf outValve) {
        this.outValve = outValve;
        LOGGER.info("OUT-VALVE binded.");
    }

    protected void unsetOutValve(OutValveDriverIf outValve) {
        this.outValve = null;
        LOGGER.info("OUT-VALVE unbinded.");
    }

    @Reference(
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.OPTIONAL
    )
    protected void setMixer(MixerDriverIf mixer) {
        this.mixerDriver = mixer;
        LOGGER.info("MIXER binded.");
    }

    protected void unsetMixer(MixerDriverIf mixer) {
        this.mixerDriver = null;
        LOGGER.info("MIXER unbinded.");
    }

    @Reference(
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.OPTIONAL
    )
    protected void setHeater(HeaterDriverIf heater) {
        this.heaterDriver = heater;
        this.heaterDriver.addHeatingCompletedListener(this);
        LOGGER.debug("Listener added");
        LOGGER.info("HEATER binded.");
    }

    protected void unsetHeater(HeaterDriverIf heater) {
        this.heaterDriver = null;
        LOGGER.info("HEATER unbinded.");
    }

}
