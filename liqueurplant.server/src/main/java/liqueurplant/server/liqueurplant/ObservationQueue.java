package liqueurplant.server.liqueurplant;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mObjectInstance;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.observation.ObservationRegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;


class ObservationQueue implements ObservationRegistryListener {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationQueue.class);

    private LinkedBlockingQueue<ObservationData> queue = new LinkedBlockingQueue<>();
    private LeshanServer server;

    public ObservationQueue(LeshanServer server) {
        this.server = server;
        server.getObservationRegistry().addListener(this);
    }

    public ObservationData take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void newObservation(Observation observation) {

    }

    @Override
    public void cancelled(Observation observation) {

    }

    @Override
    public void newValue(Observation observation, ObserveResponse response) {
        LOG.debug("Received notification from [{}] containing value [{}]", observation.getPath(),
                response.getContent().toString());

        Client client = server.getClientRegistry().findByRegistrationId(observation.getRegistrationId());

        if (client != null) {
            String path = response.getObservation().getPath().toString();

            LwM2mNode node = response.getContent();
            //If there are multiple values
            if (node instanceof LwM2mObjectInstance) {
                LwM2mObjectInstance instance = (LwM2mObjectInstance) node;
                Map<Integer, LwM2mResource> resources = instance.getResources();

                for (Map.Entry<Integer, LwM2mResource> entry : resources.entrySet()) {
                    LwM2mSingleResource value = (LwM2mSingleResource) entry.getValue();
                    String fullPath = path + '/' + value.getId();
                    queue.add(new ObservationData(client, value, fullPath));
                }
            }
            //If it's only one value
            else if (node instanceof LwM2mSingleResource) {
                LwM2mSingleResource value = (LwM2mSingleResource) node;
                queue.add(new ObservationData(client, value, path));
            }
        }
    }
}
