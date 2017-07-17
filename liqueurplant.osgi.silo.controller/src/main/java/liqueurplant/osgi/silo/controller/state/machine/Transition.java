package liqueurplant.osgi.silo.controller.state.machine;

import liqueurplant.osgi.silo.controller.api.SMReception;

public abstract class Transition extends BaseTransition<SMReception>{

    public Transition(State fromState, State toState, boolean fork, boolean join,
                      boolean completion) {
        super(fromState, toState, fork, join, completion);
    }

    public Transition(State fromState, State toState){
        super(fromState, toState);
    }

}