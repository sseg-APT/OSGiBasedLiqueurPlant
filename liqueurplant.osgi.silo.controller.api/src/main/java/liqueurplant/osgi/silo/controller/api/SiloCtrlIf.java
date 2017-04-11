package liqueurplant.osgi.silo.controller.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SiloCtrlIf {

    void put2EventQueue(SiloCtrlEvent event);

    ObservableTuple takeNotification();
}
