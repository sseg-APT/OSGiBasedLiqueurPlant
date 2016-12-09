package liqueurplant.server.liqueurplant;

import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


class ProcessA extends GenerationProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessA.class);


    ProcessA(LeshanServer server) {
        super(server);
    }

    @Override
    protected void init() {
        LOG.info("Starting {}", this.getClass().toString());

        try {
            devices.waitDevices("silo");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        generationProcessLoop();
    }


    private void generationProcessLoop() {
        Client silo = server.getClientRegistry().get("silo");

        int j = 0;
        int limit = 1000;
        long start;
        long finish;
        long table[] = new long[limit];

        LOG.info("Starting benchmark");
        for (j = 0; j < limit; j++) {
            start = System.nanoTime();
            sendRequest(new ExecuteRequest("/20000/0/5"), silo);
            finish = System.nanoTime();
            //System.out.println(j+"\t"+(finish-start));
            table[j] = finish - start;
        }
        LOG.info("Benchmark finished");

        long sum = 0;
        PrintWriter writer = null;

        try {
            writer = new PrintWriter("RTTdata.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        for (j = 0; j < limit; j++) {

            writer.println(j + "\t" + table[j] / 1000);
            sum += table[j];

        }

        writer.close();
    }

}
