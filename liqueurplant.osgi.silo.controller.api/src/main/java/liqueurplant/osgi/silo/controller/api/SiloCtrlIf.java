package liqueurplant.osgi.silo.controller.api;

import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SiloCtrlIf {

    void put2MsgQueue(BaseSignal signal);

    BaseSignal takeNotification();
}
