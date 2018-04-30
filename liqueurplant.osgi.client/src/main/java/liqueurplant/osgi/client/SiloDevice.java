package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

@Component(
        name = "liqueurplant.osgi.client",
        immediate = true,
        configurationPid = "ClientIPConfiguration",
        configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class SiloDevice extends AbstractDevice implements ManagedService {

    private static Logger LOG = LoggerFactory.getLogger(SiloObject.class);
    private SiloObject silo = new SiloObject();
    private Thread client;

    static String serverURI = "";

    public SiloDevice() {
        super("Silo4", null);
    }

    public SiloDevice(String endpoint, String[] args) {
        super(endpoint, args);
    }

    @Activate
    public void activate(Map<String, Object> properties) {
        serverURI = "coap://" + properties.get("IP") + ":" + properties.get("port");
        LOG.info("Server URI: " + serverURI);
        client = new Thread(this);
        new Thread(silo).start();
    }

    @Override
    public void updated(Dictionary<String, ?> properties) {
        if (properties == null) {
            LOG.info("No configuration found.");
        } else {
            serverURI = "coap://" + properties.get("IP") + ":" + properties.get("port");
            LOG.info("SERVER_URI updated to: " + serverURI);
            client.start();
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

    ///*
    @Reference
    protected void setSiloController(SiloCtrlIf siloCtrl) {
        silo.setSiloController(siloCtrl);
    }

    protected void unsetSiloController(SiloCtrlIf siloCtrl) {
        silo.unsetSiloController();
    }
    //*/
}
