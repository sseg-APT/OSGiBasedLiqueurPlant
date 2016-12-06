package liqueurplant.server.liqueurplant;

import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistryListener;
import org.eclipse.leshan.server.client.ClientUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


class DeviceManager implements ClientRegistryListener {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceManager.class);

    private Map<String, Client> clients = new HashMap<>();

    DeviceManager(LeshanServer server) {
        server.getClientRegistry().addListener(this);
    }

    public void waitDevices(String... devices) throws InterruptedException {

        LOG.info("Waiting for devices {}", devices.toString());

        for (String device : devices) {
            while (true) {
                synchronized (clients) {
                    if (clients.containsKey(device)) {
                        break;
                    }
                    clients.wait();
                }
            }
        }
    }

    @Override
    public void registered(Client client) {
        synchronized (clients) {
            clients.put(client.getEndpoint(), client);
            clients.notifyAll();
        }
    }

    @Override
    public void updated(ClientUpdate update, Client clientUpdated) {

    }

    @Override
    public void unregistered(Client client) {
        clients.remove(client.getEndpoint());
    }
}
