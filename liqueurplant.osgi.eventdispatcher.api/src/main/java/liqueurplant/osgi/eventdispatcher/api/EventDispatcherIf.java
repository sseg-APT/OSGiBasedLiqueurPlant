package liqueurplant.osgi.eventdispatcher.api;

import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlState;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by pBochalis on 3/16/2017.
 */
@ProviderType
public interface EventDispatcherIf {
    void setState(SiloCtrlEvent event, SiloCtrlState state);
}
