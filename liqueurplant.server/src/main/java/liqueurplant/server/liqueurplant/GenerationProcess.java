package liqueurplant.server.liqueurplant;

import org.eclipse.leshan.core.model.ResourceModel;
import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.core.request.AbstractDownlinkRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

abstract class GenerationProcess extends Thread {
    public interface GPResource {
    }

    public enum CommonObjectResources implements GPResource {
        OWNER("/16666/0/0"),
        ACQUIRE("/16666/0/1"),
        EMPTY("/16666/0/2");
        private final String stringValue;

        CommonObjectResources(final String s) {
            stringValue = s;
        }

        public String toString() {
            return stringValue;
        }
        public static Hashtable<String, String> pathToState = initializeHashTable();

        private static Hashtable<String, String> initializeHashTable() {
            Hashtable<String, String> hashtable = new Hashtable<>();
            hashtable.put(OWNER.toString(), OWNER.name());
            hashtable.put(ACQUIRE.toString(), ACQUIRE.name());
            hashtable.put(EMPTY.toString(), EMPTY.name());
            return hashtable;
        }
    }

    public enum SiloResources implements GPResource {
        STATE("/16663/0/0"),
        FILLING_COMPLETED("/16663/0/7"),
        EMPTYING_COMPLETED("/16663/0/8"),
        HEATING_COMPLETED("/16663/0/9"),
        MIXING_COMPLETED("/16663/0/10"),
        FILL("/16663/0/1"),
        EMPTY("/16663/0/2"),
        INITIALIZE("/16663/0/4"),
        HEAT("/16663/0/5"),
        MIX("/16663/0/6");
        private final String stringValue;
        public static Hashtable<String, String> pathToState = initializeHashTable();

        private static Hashtable<String, String> initializeHashTable() {
            Hashtable<String, String> hashtable = new Hashtable<>();
            hashtable.put(STATE.toString(), STATE.name());
            hashtable.put(FILLING_COMPLETED.toString(), FILLING_COMPLETED.name());
            hashtable.put(EMPTYING_COMPLETED.toString(), EMPTYING_COMPLETED.name());
            hashtable.put(HEATING_COMPLETED.toString(), HEATING_COMPLETED.name());
            hashtable.put(MIXING_COMPLETED.toString(), MIXING_COMPLETED.name());
            hashtable.put(FILL.toString(), FILL.name());
            hashtable.put(EMPTY.toString(), EMPTY.name());
            hashtable.put(INITIALIZE.toString(), INITIALIZE.name());
            hashtable.put(HEAT.toString(), HEAT.name());
            hashtable.put(MIX.toString(), MIX.name());
            return hashtable;
        }

        SiloResources(final String s) {
            stringValue = s;
        }
        public String toString() {
            return stringValue;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(GenerationProcess.class);

    protected LeshanServer server;
    protected DeviceManager devices;
    protected ObservationQueue observationQueue;

    GenerationProcess(LeshanServer server) {
        this.server = server;
        devices = new DeviceManager(server);
        observationQueue = new ObservationQueue(server);
    }

    @Override
    public void run() {
        init();
    }

    protected abstract void init();

    protected <T extends LwM2mResponse> T sendRequest(AbstractDownlinkRequest<T> request, Client client) {
        try {
            T cResponse = server.send(client, request, Config.TIMEOUT);
            if (cResponse == null) {
                LOG.warn("Request timed out.");
            } else {
                return cResponse;
            }
        } catch (InterruptedException e) {
            LOG.error("Thread Interrupted", e);
        }
        return null;
    }

    public <T> T getValueFromResource(Class<T> cls, LwM2mSingleResource resource) {
        Object value = resource.getValue();
        ResourceModel.Type type = resource.getType();

        if (cls == Boolean.class && type == ResourceModel.Type.BOOLEAN) {
            return cls.cast(Boolean.valueOf(value.toString()));
        } else if (cls == String.class && type == ResourceModel.Type.STRING) {
            return cls.cast(value.toString());
        } else if (cls == Double.class && type == ResourceModel.Type.FLOAT) {
            return cls.cast(Double.valueOf(value.toString()));
        } else {
            LOG.error("Error in resource data type");
        }

        return null;
    }

}
