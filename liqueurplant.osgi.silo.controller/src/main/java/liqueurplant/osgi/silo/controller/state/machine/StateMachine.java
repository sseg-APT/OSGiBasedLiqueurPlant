package liqueurplant.osgi.silo.controller.state.machine;

import liqueurplant.osgi.silo.controller.api.SMReception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateMachine implements Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(StateMachine.class);
    private MessageQueue branchMsgQ = null;
    private boolean eventDiscarded = false;
    private Thread itsBranchThread = null;
    private Transition activeTransition;
    private boolean forkActive = false;
    private SMReception curReception;
    private State curState = null;
    private State initState;

    protected MessageQueue itsMsgQ;

    public StateMachine(MessageQueue msgQ) {
        if (msgQ != null) itsMsgQ = msgQ;
        else itsMsgQ = new MessageQueue();
        LOGGER.info("State Machine " + this.getClass().getName() + " just started\n");
    }


    public void setInitState(State s) {
        initState = s;
    }

    @Override
    public void run() {
        curState = initState;
        // FINAL_STATE is the state machine's final state. null is used to indicate the FINAL_STATE
        while (curState != null) {
            if (!eventDiscarded) {
                // to be defined later. Probably a Thread will be activated having as run the do method
                curState.doActivity();
                LOGGER.info("current state = " + curState.getClass().getName() + "\n");
            }
            if (!curState.hasCompletionTrans()) {
                // A Completion transition is one without a trigger.
                // deactivated waiting for deferred event implementation
                curReception = itsMsgQ.getNext(curState.getDeferredEvents());
                LOGGER.info("Reception received = " + curReception.toString());
            } else
                curReception = null; // is used to activate the on completion transition

            activeTransition = curState.getActiveTransition(curReception);

            if (activeTransition == null) {
                LOGGER.info("No acive transition for reception " + curReception.toString() + " at main branch\n");
                if (false) { // check if reception is deferred and if yes keep it
                } else if (forkActive) { //  else forward event to branch if active
                    branchMsgQ.add(curReception);
                    LOGGER.info("Reception " + curReception.toString() + " dispatched to branch\n");
                } else {
                    LOGGER.error("Reception " + curReception.toString() + " is not handled at state " + curState);
                }
                eventDiscarded = true;
            } else {
                // fire transition
                if (activeTransition.hasFork()) {
                    StateMachine bsm;
                    forkActive = true;
                    branchMsgQ = new MessageQueue();
                    bsm = new StateMachine(branchMsgQ);
                    bsm.setInitState(activeTransition.branchInitState);
                    itsBranchThread = new Thread(bsm);
                    itsBranchThread.setName(Thread.currentThread().getName() + "-branchSmT");
                    itsBranchThread.start();
                }

                curState.exit();
                activeTransition.effect();
                if (activeTransition.hasJoin()) {
                    try {
                        (itsBranchThread).join();
                    } catch (InterruptedException e) {
                        LOGGER.error("InterruptedException in run(): " + e.toString());
                    }
                    forkActive = false;
                }
                curState = activeTransition.itsTargetState;
                eventDiscarded = false;
            }
        }
        LOGGER.info("State Machine " + Thread.currentThread().getName() + " terminated");

    }

}