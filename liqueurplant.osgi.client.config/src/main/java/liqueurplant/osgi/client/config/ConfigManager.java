package liqueurplant.osgi.client.config;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by bochalito on 5/4/2017.
 */
@Component(
        name = "liqueurplant.osgi.client.config",
        immediate = true
)
public class ConfigManager implements ManagedService {

    private ServiceRegistration service;

    public ConfigManager(){}

    @Activate
    public void activate(BundleContext context){
        System.out.println("ConfigManger started.");
        Dictionary props = new Hashtable();
        props.put(Constants.SERVICE_PID, "ConfigManagerService");
        service = context.registerService(ManagedService.class.getName(),
                new ConfigManager(), props);
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties == null) {
            // no configuration from configuration admin
            // or old configuration has been deleted
            System.out.println("ASDADAD");

        } else {
            // apply configuration from config admin
            System.out.println(properties.get("IP"));

        }
    }
}
