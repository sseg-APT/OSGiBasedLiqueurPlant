package liqueurplant.osgi.silo.controller.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bocha on 28/11/2016.
 */
@ProviderType
public interface SiloCtrlIf {

    enum Process2SiloCtrlEvent{
        FILL,
        EMPTY,
        START_HEATING,
        START_MIXING,
        STOP_FILLING,
        STOP_EPMTYING,
        STOP,
        START
    }

    void put2EventQueue(Object o);
}
