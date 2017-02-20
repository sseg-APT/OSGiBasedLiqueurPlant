package liqueurplant.osgi.silo;

/**
 * Created by bocha on 18/2/2017.
 */
public enum SiloCtrlState implements SiloCtrlStateMachineIf {
    EMPTY {
        @Override
        public SiloCtrlState processEvent(SiloCtrlState scState, SiloCtrlEvent e) {
            SiloCtrlState targetState;

            if(e== SiloCtrlEvent.FILL){
                targetState = SiloCtrlState.FILLING;
                performActions();
            }
            else if(e== SiloCtrlEvent.STOP){
                targetState =  SiloCtrlState.TERMINATE;
                System.out.println("Message to Generate Liqueur Proecess: siloCotrollerStopped");
            }
            else
                targetState = SiloCtrlState.EMPTY;

            //System.out.println("\tState: " + scState + " + Event: " + e + "-> " + targetState);
            return targetState;
        }

        @Override
        public void performActions() {
            // TODO Auto-generated method stub
            System.out.println("Message to Physical Silo: inValve.open");
        }
    },
    FILLING {
        @Override
        public SiloCtrlState processEvent(SiloCtrlState scState, SiloCtrlEvent e) {
            SiloCtrlState targetState;

            if(e== SiloCtrlEvent.FILLINGCOMPLETED){
                targetState =  SiloCtrlState.FULL;
                performActions();
            }
            else
                targetState =  SiloCtrlState.FILLING;

            //System.out.println("\tState: " + scState + " + Event: " + e + "-> " + targetState);
            return targetState;
        }

        @Override
        public void performActions() {
            // TODO Auto-generated method stub
            System.out.println("Message to Physical Silo: inValve.close");
            System.out.println("Message to Generate Liqueur Proecess: fillingCompleted");
        }
    },
    FULL {
        @Override
        public SiloCtrlState processEvent(SiloCtrlState scState, SiloCtrlEvent e) {
            SiloCtrlState targetState;

            if(e== SiloCtrlEvent.EMPTY){
                targetState =  SiloCtrlState.EMPTYING;
                performActions();
            }
            else
                targetState =  SiloCtrlState.FULL;

            //System.out.println("\tState: " + scState + " + Event: " + e + "-> " + targetState);
            return targetState;
        }

        @Override
        public void performActions() {
            // TODO Auto-generated method stub
            System.out.println("Message to Physical Silo: outValveValve.open");
            //System.out.println("Message to Generate Liqueur Proecess: fillingCompleted");
        }

    },
    EMPTYING {
        @Override
        public SiloCtrlState processEvent(SiloCtrlState scState, SiloCtrlEvent e) {
            SiloCtrlState targetState;

            if(e== SiloCtrlEvent.EMPTYINGCOMPLETED){
                targetState =  SiloCtrlState.EMPTY;
                performActions();
            }
            else
                targetState =  SiloCtrlState.EMPTYING;

            //System.out.println("\tState: " + scState + " + Event: " + e + "-> " + targetState);
            return targetState;
        }

        @Override
        public void performActions() {
            // TODO Auto-generated method stub
            System.out.println("Message to Physical Silo: outValve.close");
            System.out.println("Message to Generate Liqueur Process: emptyingCompleted");
        }
    },
    TERMINATE {

        @Override
        public SiloCtrlState processEvent(SiloCtrlState scState, SiloCtrlEvent e) {
            // TODO Auto-generated method stub
            performActions();
            return null;
        }

        @Override
        public void performActions() {
            // TODO Auto-generated method stub
            System.out.println("Message to Generate Liqueur Proecess: siloCotrollerStopped");
        }
    }
}

