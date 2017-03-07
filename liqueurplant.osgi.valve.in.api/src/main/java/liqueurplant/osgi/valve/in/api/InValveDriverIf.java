package liqueurplant.osgi.valve.in.api;

import org.osgi.annotation.versioning.ProviderType;


@ProviderType
public interface InValveDriverIf {

    void open() throws Exception;

    void close() throws Exception;
}
