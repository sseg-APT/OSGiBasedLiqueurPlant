package liqueurplant.valve.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bochalito on 4/12/2016.
 */
@ProviderType
public interface ValveIf {

    void open() throws Exception;

    void close() throws Exception;
}
