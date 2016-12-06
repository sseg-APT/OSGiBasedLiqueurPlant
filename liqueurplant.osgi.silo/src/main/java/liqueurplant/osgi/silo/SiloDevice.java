package liqueurplant.osgi.silo;

import liqueurplant.osgi.valve.Valve;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;

import java.io.InputStream;
import java.util.List;

/**
 * Created by bocha on 1/12/2016.
 */
public class SiloDevice extends AbstractDevice {

    Silo silo = new Silo();

    public SiloDevice(String endpoint, String[] args) {
        super(endpoint, args);
    }

    @Override
    public void init() {
        super.init();
        silo.setInValve(new Valve("IN"));
        silo.setOutValve(new Valve("OUT"));
        //new Thread(silo).start();
    }
    @Override
    protected List<LwM2mObjectEnabler> getEnablers(ObjectsInitializer initializer) {
        List<LwM2mObjectEnabler> superEnablers = super.getEnablers(initializer);
        LwM2mObjectEnabler storageEnabler = initializer.create(Silo.modelId);
        LwM2mObjectEnabler firmwareEnabler = initializer.create(FirmwareObject.modelId);
        superEnablers.add(storageEnabler);
        superEnablers.add(firmwareEnabler);
        return superEnablers;
    }

    @Override
    protected ObjectsInitializer getObjectInitializer() {
        ObjectsInitializer initializer = super.getObjectInitializer();
        initializer.setInstancesForObject(Silo.modelId, silo);
        initializer.setClassForObject(FirmwareObject.modelId, FirmwareObject.class);
        return initializer;
    }

    @Override
    protected LwM2mModel getLwM2mModel() {
        InputStream a = this.getClass().getResourceAsStream("/objects/oma-objects-spec.json");
        return new LwM2mModel(ObjectLoader.loadJsonStream(a));
    }
}
