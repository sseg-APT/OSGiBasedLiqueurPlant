package liqueurplant.osgi.silo.controller.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SiloCtrlIf {

    void put2MsgQueue(SimpleSiloSMEvent event);

    ObservableTuple takeNotification();
}
