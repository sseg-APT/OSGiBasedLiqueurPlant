package liqueurplant.server.liqueurplant;

import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ProcessA extends GenerationProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessA.class);


    ProcessA(LeshanServer server) {
        super(server);
    }

    @Override
    protected void init() {
        LOG.info("Starting {}", this.getClass().toString());
        generationProcessLoop();
    }


    private void generationProcessLoop() {
        // set the first state to be the initialization state

        while (true) {
            try {
                ObservationData newData = observationQueue.take();

            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
