package liqueurplant.osgi.client.config;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    public ConfigManager(){}

    @Activate
    public void activate(BundleContext context){
        LOGGER.info("ConfigManager started.");
        Dictionary props = new Hashtable();
        props.put(Constants.SERVICE_PID, "ConfigManagerService");
        service = context.registerService(ManagedService.class.getName(),
                new ConfigManager(), props);
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties == null) {
            LOGGER.warn("No configuration found.");

        } else {
            // apply configuration from config admin
            LOGGER.info("IP: " + properties.get("IP"));
            LOGGER.info("Port: "  + properties.get("port"));

        }
    }
}
