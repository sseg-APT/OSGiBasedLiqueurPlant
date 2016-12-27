package liqueurplant.osgi.silo.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bocha on 28/11/2016.
 */
@ProviderType
//SiloCtrlIf + heat2temp, getState, mix
public interface SiloCtrlIf {

    void fill() throws Exception;

    void empty() throws Exception;
}
