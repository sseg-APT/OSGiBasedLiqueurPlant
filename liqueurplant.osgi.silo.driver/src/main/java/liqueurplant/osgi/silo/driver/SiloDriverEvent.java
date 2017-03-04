package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.silo.SiloState;
import liqueurplant.osgi.silo.controller.SiloCtrlEvent;

/**
 * Created by bocha on 1/3/2017.
 */
public enum SiloDriverEvent {
    INVALVE_OPEN {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.FILLING.toString());
            System.out.println("[Silo Driver]: " + SiloState.FILLING.toString());
            try {
                SiloDriverActivator.inValve.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    },
    INVALVE_CLOSE {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.FULL.toString());
            System.out.println("[Silo Driver]: " + SiloState.FULL.toString());
            try {
                SiloDriverActivator.inValve.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    },
    OUTVALVE_OPEN {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.EMPTYING.toString());
            System.out.println("[Silo Driver]: " + SiloState.EMPTYING.toString());
        }
    },
    OUTVALVE_CLOSE {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.EMPTY.toString());
            System.out.println("[Silo Driver]: " + SiloState.EMPTY.toString());
        }
    },
    STOP {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.OFF.toString());
            System.out.println("[Silo Driver]: " + SiloState.OFF.toString());
        }
    },
    LOW_LEVEL_REACHED {
        @Override
        public void sendEvent(SiloDriver d) {
            try {
                d.itsCtrlEq.put(SiloCtrlEvent.LOW_LEVEL_REACHED);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //*/
        }
    },
    HIGH_LEVEL_REACHED {
        @Override
        public void sendEvent(SiloDriver d) {
            try {
                d.itsCtrlEq.put(SiloCtrlEvent.HIGH_LEVEL_REACHED);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //*/
        }
    };

    abstract void sendEvent(SiloDriver d);
}
