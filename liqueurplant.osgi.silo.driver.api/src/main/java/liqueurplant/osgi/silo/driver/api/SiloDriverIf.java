package liqueurplant.osgi.silo.driver.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by pBochalis on 3/8/2017.
 */
@ProviderType
public interface SiloDriverIf {

    enum Driver2SiloEvent{
        HIGH_LEVEL_REACHED,
        LOW_LEVEL_REACHED
    }

}
