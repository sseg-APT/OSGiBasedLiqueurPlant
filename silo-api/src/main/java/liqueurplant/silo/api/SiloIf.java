package liqueurplant.silo.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bocha on 28/11/2016.
 */
@ProviderType
public interface SiloIf {

    void fill() throws Exception;

    void empty() throws Exception;
}
