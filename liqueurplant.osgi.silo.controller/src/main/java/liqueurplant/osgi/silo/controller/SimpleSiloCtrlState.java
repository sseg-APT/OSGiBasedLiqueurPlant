package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.Process2SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlState;
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
            // TODO Auto-generated method stub
            if (e == Process2SiloCtrlEvent.STOP) {
                try {
                    ctrl.eventQueue.put(Process2SiloCtrlEvent.STOP);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            try {
                ctrl.inValve.open();
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
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
            // TODO Auto-generated method stub
            try {
                ctrl.inValve.close();
                if (e == Driver2SiloCtrlEvent.HIGH_LEVEL_REACHED) {
                    ctrl.fillingCompleted = true;
                }
                //ctrl.itsPlant.lgpA.itsEq.put(Ctrl2LGPTypeAEvent.S1_FILLINGCOMPLETED);

            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
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
            // TODO Auto-generated method stub

            try {
                ctrl.outValve.open();
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
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
            // TODO Auto-generated method stub
            try {

                ctrl.outValve.close();
                if (e == Driver2SiloCtrlEvent.LOW_LEVEL_REACHED) {
                    ctrl.fillingCompleted = false;
                    //send emptying completed
                }
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
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
            // TODO Auto-generated method stub
            if (e == Process2SiloCtrlEvent.START) {
            }
            if (e == Process2SiloCtrlEvent.STOP) {
                /*
                try {
                    //ctrl.itsDriver.itsEq.put(Ctrl2SiloDriverEvent.STOP);

                } catch (InterruptedException ie) {
                    // TODO Auto-generated catch block
                    ie.printStackTrace();
                }
                */
            }
        }
    }
}
