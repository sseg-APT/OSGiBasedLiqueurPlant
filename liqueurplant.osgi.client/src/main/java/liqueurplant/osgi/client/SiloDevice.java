package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Component(
        name = "liqueurplant.osgi.client",
        immediate = true,
        configurationPid = "ConfigManagerService",
        configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class SiloDevice extends AbstractDevice implements ManagedService {

    static String serverURI = "";
    private String IP = "";
    private String port = "";
    private ServiceRegistration configService;
    private Logger LOGGER = LoggerFactory.getLogger(AbstractDevice.class);
    private BundleContext context;

    SiloObject silo = new SiloObject();

    public SiloDevice() {
        super("silo", null);
    }

    public SiloDevice(String endpoint, String[] args) {
        super(endpoint, args);
    }

    @Activate
    public void activate(Map<String, Object> properties) {
        this.context = context;
        LOGGER.info("Silo device activated.");
        serverURI = "coap://" + properties.get("IP") + ":" + properties.get("port");
        System.out.println(serverURI);
        new Thread(this).start();
        new Thread(silo).start();
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties == null) {
            LOGGER.warn("No configuration found.");
        } else {
            LOGGER.info("IP: " + properties.get("IP"));
            this.IP = properties.get("IP").toString();
            LOGGER.info("Port: " + properties.get("port"));
            this.port = properties.get("port").toString();
            serverURI = "coap://" + this.IP + ":" + this.port;
            LOGGER.info("Server URI: " + serverURI);
        }
    }

    @Override
    protected List<LwM2mObjectEnabler> getEnablers(ObjectsInitializer initializer) {
        List<LwM2mObjectEnabler> superEnablers = super.getEnablers(initializer);
        LwM2mObjectEnabler storageEnabler = initializer.create(SiloObject.modelId);
        LwM2mObjectEnabler firmwareEnabler = initializer.create(FirmwareObject.modelId);
        superEnablers.add(storageEnabler);
        superEnablers.add(firmwareEnabler);
        return superEnablers;
    }

    @Override
    protected ObjectsInitializer getObjectInitializer() {
        ObjectsInitializer initializer = super.getObjectInitializer();
        initializer.setInstancesForObject(SiloObject.modelId, silo);
        initializer.setClassForObject(FirmwareObject.modelId, FirmwareObject.class);
        return initializer;
    }

    @Reference
    protected void setSiloController(SiloCtrlIf siloCtrl) {
        silo.setSiloController(siloCtrl);
    }

    protected void unsetSiloController(SiloCtrlIf siloCtrl) {
        silo.unsetSiloController();
    }
}
