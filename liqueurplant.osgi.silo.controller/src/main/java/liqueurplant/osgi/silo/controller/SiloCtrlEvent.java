package liqueurplant.osgi.silo.controller;

/**
 * Created by bocha on 18/2/2017.
 */
public enum SiloCtrlEvent {
    FILL,
    HIGH_LEVEL_REACHED,
    EMPTY,
    LOW_LEVEL_REACHED,
    START_HEATING,
    HEATING_COMPLETED,
    START_MIXING,
    MIXING_COMPLETED,
    STOP_FILLING,
    STOP_EPMTYING,
    STOP,
    START
}
