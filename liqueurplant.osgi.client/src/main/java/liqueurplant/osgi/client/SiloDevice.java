package liqueurplant.osgi.client;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;


@Component(
        name = "liqueurplant.osgi.client",
        immediate = true
)
public class SiloDevice extends AbstractDevice{

    SiloObject silo = new SiloObject();

    public SiloDevice() { super("silo", null); }

    public SiloDevice(String endpoint, String[] args) {
        super(endpoint, args);
    }

    @Activate
    public void activate(BundleContext context) {
        System.out.println("Silo device activated.");
        new Thread(this).start();
        new Thread(silo).start();

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
