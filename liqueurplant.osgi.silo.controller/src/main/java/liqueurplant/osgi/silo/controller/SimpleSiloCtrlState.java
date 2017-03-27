package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.*;
import liqueurplant.osgi.silo.driver.api.Driver2SiloCtrlEvent;

/**
 * Created by pBochalis on 3/13/2017.
 */
public enum SimpleSiloCtrlState implements SimpleSiloCtrlStateMachineIf, SiloCtrlState {
    EMPTY {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if (e == Process2SiloCtrlEvent.FILL) {
                targetState = SimpleSiloCtrlState.FILLING;
                performActions(ctrl, e);
            } else if (e == Process2SiloCtrlEvent.STOP) {
                targetState = SimpleSiloCtrlState.IDLE;
            } else
                targetState = SimpleSiloCtrlState.EMPTY;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            if (e == Process2SiloCtrlEvent.STOP) {
                try {
                    ctrl.eventQueue.put(Process2SiloCtrlEvent.STOP);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                ctrl.inValve.open();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    },
    FILLING {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if (e == Driver2SiloCtrlEvent.HIGH_LEVEL_REACHED || e == Process2SiloCtrlEvent.STOP_FILLING) {
                targetState = SimpleSiloCtrlState.FULL;
                performActions(ctrl, e);
            } else
                targetState = SimpleSiloCtrlState.FILLING;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            try {
                ctrl.inValve.close();
                if (e == Driver2SiloCtrlEvent.HIGH_LEVEL_REACHED)
                    ctrl.notificationQueue.put(
                            new ObservableTuple(
                                    Ctrl2WrapperEvent.FILLING_COMPLETED,SimpleSiloCtrlState.FULL)
                    );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    },
    FULL {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if (e == Process2SiloCtrlEvent.EMPTY) {
                targetState = SimpleSiloCtrlState.EMPTYING;
                performActions(ctrl, e);
            } else
                targetState = SimpleSiloCtrlState.FULL;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            try {
                ctrl.outValve.open();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    },
    EMPTYING {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if (e == Driver2SiloCtrlEvent.LOW_LEVEL_REACHED || e == Process2SiloCtrlEvent.STOP_EPMTYING) {
                targetState = SimpleSiloCtrlState.EMPTY;
                performActions(ctrl, e);
            } else
                targetState = SimpleSiloCtrlState.EMPTYING;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            try {
                ctrl.outValve.close();
                if(e == Driver2SiloCtrlEvent.HIGH_LEVEL_REACHED) {
                    ctrl.notificationQueue.put(
                            new ObservableTuple(
                                    Ctrl2WrapperEvent.EMPTYING_COMPLETED, SimpleSiloCtrlState.EMPTY)

                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    },
    IDLE {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            SimpleSiloCtrlState targetState;

            if (e == Process2SiloCtrlEvent.STOP) {
                targetState = null;
            } else if (e == Process2SiloCtrlEvent.START) {
                targetState = SimpleSiloCtrlState.EMPTY;
                performActions(ctrl, e);
            } else
                targetState = SimpleSiloCtrlState.IDLE;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            if (e == Process2SiloCtrlEvent.START) {
            }
            if (e == Process2SiloCtrlEvent.STOP) {
            }
        }
    }
}
