package liqueurplant.osgi.silo.controller.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bocha on 28/11/2016.
 */
@ProviderType
//SiloCtrlIf + heat2temp, getState, mix
public interface SiloCtrlIf {

    void put2EventQueue(String event);
}
