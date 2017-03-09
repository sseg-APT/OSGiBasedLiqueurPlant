package liqueurplant.osgi.silo.driver.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by pBochalis on 3/8/2017.
 */
@ProviderType
public interface SiloDriverIf {

    void openDriver();

    void closeDriver();

    String highLevelReached() throws Exception;

    String lowLevelReached() throws Exception;

    enum Event{
        HIGH_LEVEL_REACHED,
        LOW_LEVEL_REACHED
    }

}
