package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.*;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;


@Component(
        name = "liqueurplant.osgi.client",
        immediate = true,
        configurationPid = "liqueurplant.osgi.client.SiloDevice"
)
public class SiloDevice extends AbstractDevice implements ManagedService {

    SiloObject silo = new SiloObject();
    private static final String PROP_HOST = "host";
    private static final String PROP_PORT = "port";

    private volatile String host;
    private volatile Integer port;


    public SiloDevice() {
        super("silo", null);


    }

    public SiloDevice(String endpoint, String[] args) {
        super(endpoint, args);
    }

    @interface Config{
        int port() default 8080;
        String host();
    }

    @Activate
    public void activate(Map<String,Object> map) {
        System.out.println("Silo device activated.");
        System.out.println("Configuration " + map);
        new Thread(this).start();
        new Thread(silo).start();
    }

    @Modified
    void modified(Config config) {
        SiloObject.LOG.debug("Configuration " + config.host() + ":" + config.port());
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

    @Override
    public void updated(Dictionary<String, ?> dictionary) throws ConfigurationException {
        SiloObject.LOG.info("Leshan server connection updated: " + dictionary);
        if (dictionary == null) {
            host = null;
            port = null;
            SiloObject.LOG.info("Unconfigured Leshan server connection.");
        }
        else {
            host = (String) dictionary.get(PROP_HOST);
            if (host == null) {
                throw new ConfigurationException(PROP_HOST, "Mandatory field");
            }
            String portStr = (String) dictionary.get(PROP_PORT);
            if (portStr == null) {
                throw new ConfigurationException(PROP_PORT, "Mandatory field");
            }
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException ex) {
                throw new ConfigurationException(PROP_PORT, "Invalid number", ex);
            }
            SiloObject.LOG.info("Configured server connection for host: " + host + ", port: " + port);
        }

    }

    @Reference
    protected void setSiloController(SiloCtrlIf siloCtrl) {
        silo.setSiloController(siloCtrl);
    }

    protected void unsetSiloController(SiloCtrlIf siloCtrl) {
        silo.unsetSiloController();
    }



}
