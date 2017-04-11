package liqueurplant.osgi.valve.out.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface OutValveDriverIf {

    void open() throws Exception;

    void close() throws Exception;
}
