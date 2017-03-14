package liqueurplant.osgi.silo.driver.api;

import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;

/**
 * Created by pBochalis on 3/14/2017.
 */
public enum Driver2SiloCtrlEvent implements SiloCtrlEvent {
    HIGH_LEVEL_REACHED,
    LOW_LEVEL_REACHED
}
