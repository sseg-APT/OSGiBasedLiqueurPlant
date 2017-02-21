package liqueurplant.osgi.client;

import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;

import java.util.List;

/**
 * Created by bocha on 1/12/2016.
 */
public class SiloDevice extends AbstractDevice {

    SiloObject silo = new SiloObject();

    public SiloDevice(String endpoint, String[] args) {
        super(endpoint, args);
    }

    @Override
    public void init() {
        super.init();

        //new Thread(silo).start();
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

}
