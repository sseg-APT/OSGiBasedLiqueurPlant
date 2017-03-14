package liqueurplant.osgi.silo.controller.api;

import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;

/**
 * Created by pBochalis on 3/14/2017.
 */
public enum Process2SiloCtrlEvent implements SiloCtrlEvent {
    FILL,
    EMPTY,
    START_HEATING,
    START_MIXING,
    STOP_FILLING,
    STOP_EPMTYING,
    STOP,
    START
}