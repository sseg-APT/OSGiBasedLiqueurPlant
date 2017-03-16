package liqueurplant.osgi.silo.controller.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bocha on 28/11/2016.
 */
@ProviderType
public interface SiloCtrlIf {

    void put2EventQueue(SiloCtrlEvent event);

    boolean getFillingCompleted();

}
