package liqueurplant.osgi.silo.controller.state.machine;

import liqueurplant.osgi.silo.controller.api.SMReception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateMachine implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StateMachine.class);
    public MessageQueue<SMReception> itsMsgQ;
    BaseState initState;
    SMReception curReception;
    BaseState curState = null;
    boolean eventDiscarded = false;
    BaseTransition activeTransition;
    // fork impl
    public MessageQueue branchMsgQ = null;

    boolean forkActive = false;
    Thread itsBranchThread = null;
    StateMachine branch;


    public StateMachine(MessageQueue msgQ) {
        if (msgQ != null) itsMsgQ = msgQ;        //for branch SMs
        else
            itsMsgQ = new MessageQueue();
    }


    public void setInitState(BaseState s) {
        initState = s;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        curState = initState;
        curState.entry();
        while (curState != null) {        // FINAL_STATE is the state machine's final state. null is used to indicate the FINAL_STATE
            if (!eventDiscarded) {
                curState.doActivity();    // to be defined later. Probably a Thread will be activated having as run the do method
                //LOG.info("current state = " + curState.getClass().getName() + "\n");
            }
            if (!curState.hasCompletionTrans()) {        // a Completion transition is one without a trigger.
                // deactivated waiting for deferred event implementation
                // if(getValidDeferredEvent()!=null){ // gets a valid for the current state event from the deferred Pool, if any
                //  returns a valid for the current state deferred event for further processing
                //} else {
                curReception = itsMsgQ.getNext(curState.getDeferredEvents());
                //LOG.info("Reception received = " + curReception.toString());
                //}
            } else
                curReception = null;        // is used to activate the on completion transition

            activeTransition = curState.getActiveTransition(curReception);

            if (activeTransition == null) {
                //LOG.error("Reception " + curReception.toString() + " is not handled at state " + curState);
                eventDiscarded = true;
            } else {    // fire transition
                curState.exit();
                activeTransition.effect();
                if (activeTransition.hasFork()) {
                    StateMachine bsm;
                    forkActive = true;
                    branchMsgQ = new MessageQueue();
                    itsMsgQ.addChildQueue(branchMsgQ);
                    bsm = new StateMachine(branchMsgQ);
                    bsm.setInitState(activeTransition.branchInitState);
                    itsBranchThread = new Thread(bsm);
                    itsBranchThread.setName(Thread.currentThread().getName() + "-branchSmT");
                    itsBranchThread.start();
                }

                if (activeTransition.hasJoin()) {
                    try {
                        LOG.info("Main thread is waiting branch to finish");
                        (itsBranchThread).join();
                        itsMsgQ.removeChildQueue(branchMsgQ);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    forkActive = false;
                }
                curState = activeTransition.itsTargetState;
                eventDiscarded = false;
                if (curState != null) {
                    curState.entry();
                }
            }
        }
        LOG.info("State Machine " + Thread.currentThread().getName() + " terminated");

    }

}
