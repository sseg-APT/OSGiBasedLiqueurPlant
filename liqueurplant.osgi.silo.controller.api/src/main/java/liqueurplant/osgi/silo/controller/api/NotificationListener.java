package liqueurplant.osgi.silo.controller.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bochalito on 24/3/2017.
 */
@ProviderType
public interface NotificationListener {

    void updateNotification(ObservableTuple observable);
}
