package liqueurplant.osgi.silo.controller.api;

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