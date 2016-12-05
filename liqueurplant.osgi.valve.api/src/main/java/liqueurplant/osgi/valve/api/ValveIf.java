package liqueurplant.osgi.valve.api;

import org.osgi.annotation.versioning.ProviderType;


@ProviderType
public interface ValveIf {

    void open() throws Exception;

    void close() throws Exception;
}
