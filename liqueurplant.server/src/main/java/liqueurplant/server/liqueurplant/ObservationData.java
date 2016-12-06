package liqueurplant.server.liqueurplant;


import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.server.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ObservationData {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationData.class);

    private Client client;
    private LwM2mSingleResource resource;
    private String path;

    public ObservationData(Client client, LwM2mSingleResource resource, String path) {
        this.client = client;
        this.resource = resource;
        this.path = path;
    }

    public Client getClient() {
        return client;
    }

    public Object getValue() {
        return resource.getValue();
    }

    public String getPath() {
        return path;
    }

    public LwM2mSingleResource getResource() { return resource; }

}
