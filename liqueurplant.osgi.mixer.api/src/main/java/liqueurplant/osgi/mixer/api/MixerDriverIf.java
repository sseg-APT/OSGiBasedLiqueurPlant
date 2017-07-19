package liqueurplant.osgi.mixer.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface MixerDriverIf {

    void start();

    void stop();
}
