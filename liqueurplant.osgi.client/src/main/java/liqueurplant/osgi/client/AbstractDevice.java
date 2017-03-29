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
import java.util.Random;

import static org.eclipse.leshan.LwM2mId.DEVICE;
import static org.eclipse.leshan.LwM2mId.SECURITY;
import static org.eclipse.leshan.LwM2mId.SERVER;
import static org.eclipse.leshan.client.object.Security.noSec;
import static org.eclipse.leshan.client.object.Security.noSecBootstap;
import static org.eclipse.leshan.client.object.Security.psk;
import static org.eclipse.leshan.client.object.Security.pskBootstrap;

public abstract class AbstractDevice implements Runnable {
    String endpoint, localAddress, secureLocalAddress, serverURI;
    int localPort, secureLocalPort;
    boolean needBootstrap;
    byte[] pskIdentity, pskKey;

    private static final String USAGE = "java -jar [filename] [OPTIONS]";

    public AbstractDevice(String endpoint, String[] args) {

        serverURI = "coap://192.168.1.6:5683";

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
        this.serverURI = serverURI;
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
        final LeshanClient client = builder.build();


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
                initializer.setInstancesForObject(SECURITY, noSecBootstap(serverURI));
            else
                initializer.setInstancesForObject(SECURITY, pskBootstrap(serverURI, pskIdentity, pskKey));
        } else {
            if (pskIdentity == null) {
                initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
                initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
            } else {
                initializer.setInstancesForObject(SECURITY, psk(serverURI, 123, pskIdentity, pskKey));
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