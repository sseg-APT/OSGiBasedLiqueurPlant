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
import java.lang.ref.WeakReference;


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

    public static void gc() {
        Object obj = new Object();
        WeakReference ref = new WeakReference<Object>(obj);
        obj = null;
        while(ref.get() != null) {
            System.gc();
        }
    }

    private void generationProcessLoop() {
        Client silo = server.getClientRegistry().get("silo");

        int j = 0;
        int limit = 1000;
        long start;
        long finish;
        long table[] = new long[limit];

        LOG.info("Starting benchmark");
        ExecuteRequest request = new ExecuteRequest("/20000/0/5");
        System.gc();
        for (j = 0; j < limit; j++) {
            start = System.nanoTime();
            try {
                server.send(silo, request, Config.TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
