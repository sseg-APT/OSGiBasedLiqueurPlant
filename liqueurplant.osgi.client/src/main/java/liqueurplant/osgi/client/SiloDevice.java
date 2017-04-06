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
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;


@Component(
        name = "liqueurplant.osgi.client",
        immediate = true
)
public class SiloDevice extends AbstractDevice implements ManagedService {

    SiloObject silo = new SiloObject();
    static String serverURI = "coap://0.0.0.0:5683";

    private Logger LOGGER = LoggerFactory.getLogger(AbstractDevice.class);
    private ServiceRegistration configService;
    private String IP = "";
    private String port = "";


    public SiloDevice(){
        super("silo", null);
    }

    //public SiloDevice() {
    //    super(serverURI,"silo", null);
    //}

    public SiloDevice(String serverURI,String endpoint, String[] args) {
        super(endpoint, args);
    }

    @Activate
    public void activate(BundleContext context) {
        LOGGER.info("Silo device activated.");
        Dictionary props = new Hashtable();
        props.put(Constants.SERVICE_PID, "ConfigManagerService");
        configService = context.registerService(ManagedService.class.getName(),
                new SiloDevice(), props);
        new Thread(this).start();
        new Thread(silo).start();

    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties == null) {
            LOGGER.warn("No configuration found.");
        } else {
            // apply configuration from config admin
            LOGGER.info("IP: " + properties.get("IP"));
            this.IP = properties.get("IP").toString();
            LOGGER.info("Port: "  + properties.get("port"));
            this.port = properties.get("port").toString();
            serverURI = "coap://" + this.IP + ":" + this.port;

            new SiloDevice(serverURI, "silo", null);
            //LOGGER.info("ServerURI: " + serverURI);
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
