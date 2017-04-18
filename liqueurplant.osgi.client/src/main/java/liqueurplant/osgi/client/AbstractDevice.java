package liqueurplant.osgi.client;

import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.request.BindingMode;

import java.io.InputStream;
import java.util.List;

import static org.eclipse.leshan.LwM2mId.*;
import static org.eclipse.leshan.client.object.Security.*;

public abstract class AbstractDevice implements Runnable {

    private static final String USAGE = "java -jar [filename] [OPTIONS]";
    private int localPort, secureLocalPort;
    private boolean needBootstrap;
    private byte[] pskIdentity, pskKey;
    private String endpoint, localAddress, secureLocalAddress;
    static LeshanClient client;


    public AbstractDevice(String endpoint, String[] args) {

        // get security info
        byte[] pskIdentity = null;
        byte[] pskKey = null;

        // get local address
        String localAddress = null;

        // get secure local address
        String secureLocalAddress = null;
        int secureLocalPort = 0;

        this.endpoint = endpoint;
        this.localAddress = localAddress;
        this.localPort = 58464;
        this.secureLocalAddress = secureLocalAddress;
        this.secureLocalPort = secureLocalPort;
        this.needBootstrap = false;
        //this.serverURI = serverURI;
        this.pskIdentity = pskIdentity;
        this.pskKey = pskKey;



    }
    @Override
    public void run() {
        List<LwM2mObjectEnabler> enablers = this.createObjects();
        // Create client
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress(localAddress, localPort);
        builder.setLocalSecureAddress(secureLocalAddress, secureLocalPort);
        builder.setObjects(enablers);
        client = builder.build();


        // Start the client
        client.start();

        // De-register on shutdown and stop client.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                client.destroy(true); // send de-registration request before destroy
            }
        });
    }

    protected List<LwM2mObjectEnabler> createObjects() {

        ObjectsInitializer initializer = getObjectInitializer();
        return getEnablers(initializer);
    }

    protected List<LwM2mObjectEnabler> getEnablers(ObjectsInitializer initializer) {
        List<LwM2mObjectEnabler> enablers = initializer.create(SECURITY, SERVER, DEVICE);
        return enablers;
    }

    protected ObjectsInitializer getObjectInitializer() {
        // Initialize object list
        ObjectsInitializer initializer;
        LwM2mModel model = getLwM2mModel();
        if (model == null) {
            initializer = new ObjectsInitializer();
        } else {
            initializer = new ObjectsInitializer(model);
        }

        if (needBootstrap) {
            if (pskIdentity == null)
                initializer.setInstancesForObject(SECURITY, noSecBootstap(SiloDevice.serverURI));
            else
                initializer.setInstancesForObject(SECURITY, pskBootstrap(SiloDevice.serverURI, pskIdentity, pskKey));
        } else {
            if (pskIdentity == null) {
                initializer.setInstancesForObject(SECURITY, noSec(SiloDevice.serverURI, 123));
                initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
            } else {
                initializer.setInstancesForObject(SECURITY, psk(SiloDevice.serverURI, 123, pskIdentity, pskKey));
                initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
            }
        }
        initializer.setInstancesForObject(DEVICE, new Device("Test", "Test", "Test", "U"));
        return initializer;
    }

    protected LwM2mModel getLwM2mModel() {
        InputStream defaultSpec = this.getClass().getResourceAsStream("/objects/oma-objects-spec.json");
        InputStream liqueurSpec = this.getClass().getResourceAsStream("/objects/liqueur-plant.json");
        List<ObjectModel> models = ObjectLoader.loadJsonStream(defaultSpec);
        models.addAll(ObjectLoader.loadJsonStream(liqueurSpec));
        return new LwM2mModel(models);
    }

}