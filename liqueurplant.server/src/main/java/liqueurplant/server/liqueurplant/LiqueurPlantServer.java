package liqueurplant.server.liqueurplant;

import liqueurplant.server.LeshanServerDemo;

import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiqueurPlantServer {

    private static final Logger LOG = LoggerFactory.getLogger(LeshanServerDemo.class);

    LeshanServer server;

    public LiqueurPlantServer(LeshanServer server) {
        this.server = server;
    }

    public void run() {

        LOG.debug("All silos have connected");

        ProcessA generationProcessA = new ProcessA(server);

        // start the generation processes
        generationProcessA.start();

    }


}
