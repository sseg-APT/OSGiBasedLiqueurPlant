package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.SiloEvent;
import liqueurplant.osgi.silo.driver.SiloDriverEvent;

/**
 * Created by bocha on 18/2/2017.
 */
public enum SimpleSiloCtrlState implements SimpleSiloCtrlStateMachineIf {
    EMPTY {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if(e== SiloCtrlEvent.FILL){
                targetState = SimpleSiloCtrlState.FILLING;
                performActions(ctrl,e);
            }
            else if(e== SiloCtrlEvent.STOP){
                targetState =  SimpleSiloCtrlState.IDLE;
                System.out.println("S1 Ctrl: stopped"); // Logger Severe
            }
            else
                targetState = SimpleSiloCtrlState.EMPTY;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            if(e== SiloCtrlEvent.STOP){
                System.out.println("S1 Ctrl: STOP accepted"); // Logger Severe
                try {
                    ctrl.itsEq.put(SiloCtrlEvent.STOP);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            System.out.println("S1 Ctrl Action: Message to Silo driver=" + SiloDriverEvent.INVALVE_OPEN); // Logger Finer
            try {
                ctrl.itsDriver.itsEq.put(SiloDriverEvent.INVALVE_OPEN);
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            }
        }
    },
    FILLING {
        @Override
        public SimpleSiloCtrlState processEvent (SimpleSiloCtrl ctrl,SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if(e== SiloCtrlEvent.HIGH_LEVEL_REACHED || e== SiloCtrlEvent.STOP_FILLING){
                targetState =  SimpleSiloCtrlState.FULL;
                performActions(ctrl,e);
            }
            else
                targetState =  SimpleSiloCtrlState.FILLING;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            System.out.println("S1 Ctrl Action: Message to Silo driver =" + SiloDriverEvent.INVALVE_CLOSE); // Logger Finer
            try {
                ctrl.itsDriver.itsEq.put(SiloDriverEvent.INVALVE_CLOSE );
                System.out.println("S1 Ctrl Action: Message to lgpA =" + SiloEvent.S1_FILLINGCOMPLETED); // Logger Fine
                //if(e==SiloCtrlEvent.HIGH_LEVEL_REACHED)
                    //ctrl.itsPlant.lgpA.itsEq.put(LGPTypeAEvent.S1_FILLINGCOMPLETED);

            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            }

        }
    },
    FULL {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if(e== SiloCtrlEvent.EMPTY){
                targetState =  SimpleSiloCtrlState.EMPTYING;
                performActions(ctrl,e);
            }
            else
                targetState =  SimpleSiloCtrlState.FULL;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub

            System.out.println("S1 Ctrl Action: Message to Silo driver =" + SiloDriverEvent.OUTVALVE_OPEN); // Logger finer
            try {
                ctrl.itsDriver.itsEq.put(SiloDriverEvent.OUTVALVE_OPEN);
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            }
        }

    },
    EMPTYING {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            SimpleSiloCtrlState targetState;

            if(e== SiloCtrlEvent.LOW_LEVEL_REACHED || e== SiloCtrlEvent.STOP_EPMTYING ){
                targetState =  SimpleSiloCtrlState.EMPTY;
                performActions(ctrl,e);
            }
            else
                targetState =  SimpleSiloCtrlState.EMPTYING;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            System.out.println("S1 Ctrl Action: Message to Silo driver =" + SiloDriverEvent.OUTVALVE_CLOSE); //Logger finer
            try {
                ctrl.itsDriver.itsEq.put(SiloDriverEvent.OUTVALVE_CLOSE);
                //if(e== SiloCtrlEvent.LOW_LEVEL_REACHED)
                    //ctrl.itsPlant.lgpA.itsEq.put(LGPTypeAEvent.S1_EMPTYINGCOMPLETED);
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            }
            System.out.println("S1 Ctrl Action: Message to lgpA =" + SiloEvent.S1_EMPTYINGCOMPLETED); // Logger fienr
        }
    },
    IDLE {
        @Override
        public SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            SimpleSiloCtrlState targetState;

            if(e== SiloCtrlEvent.STOP){
                targetState =  null;
            }else if(e== SiloCtrlEvent.START){
                targetState =  SimpleSiloCtrlState.EMPTY;
                performActions(ctrl,e);
            }
            else
                targetState =  SimpleSiloCtrlState.IDLE;

            return targetState;
        }

        @Override
        public void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            if(e== SiloCtrlEvent.START){
                System.out.println("S1 Ctrl: started"); // Logger Info
            }
            if(e== SiloCtrlEvent.STOP){
                try {
                    ctrl.itsDriver.itsEq.put(SiloDriverEvent.STOP);
                } catch (InterruptedException ie) {
                    // TODO Auto-generated catch block
                    ie.printStackTrace();
                }
                System.out.println("S1 Ctrl: stopped"); // Logger Severe
            }
        }
    }
}
