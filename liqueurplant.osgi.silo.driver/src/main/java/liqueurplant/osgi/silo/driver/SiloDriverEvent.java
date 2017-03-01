package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.plant.LiqueurPlant;
import liqueurplant.osgi.plant.PSiloState;
import liqueurplant.osgi.silo.controller.SiloCtrlEvent;

/**
 * Created by bocha on 1/3/2017.
 */
public enum SiloDriverEvent {
    INVALVE_OPEN {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.FILLING.toString());
            System.out.println("[Silo Driver]: " + PSiloState.FILLING.toString());
        }
    },
    INVALVE_CLOSE {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.FULL.toString());
            System.out.println("[Silo Driver]: " + PSiloState.FULL.toString());
        }
    },
    OUTVALVE_OPEN {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.EMPTYING.toString());
            System.out.println("[Silo Driver]: " + PSiloState.EMPTYING.toString());
        }
    },
    OUTVALVE_CLOSE {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.EMPTY.toString());
            System.out.println("[Silo Driver]: " + PSiloState.EMPTY.toString());
        }
    },
    STOP {
        @Override
        public void sendEvent(SiloDriver d) {
            //d.itsPlantState.setText(PSiloState.OFF.toString());
            System.out.println("[Silo Driver]: " + PSiloState.OFF.toString());
            LiqueurPlant.LOGGER.severe("Physical silo out of control");
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
