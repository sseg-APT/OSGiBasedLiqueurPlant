package liqueurplant.osgi.valve.out.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by pBochalis on 3/6/2017.
 */
@ProviderType
public interface OutValveDriverIf {

    void open() throws Exception;

    void close() throws Exception;
}
