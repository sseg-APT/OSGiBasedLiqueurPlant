package liqueurplant.osgi.silo.controller.api;

import com.sun.org.apache.bcel.internal.generic.NOP;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Created by bocha on 28/11/2016.
 */
@ProviderType
public interface SiloCtrlIf {

    void put2EventQueue(SiloCtrlEvent event);

    void addListener(NotificationListener listener);

}
