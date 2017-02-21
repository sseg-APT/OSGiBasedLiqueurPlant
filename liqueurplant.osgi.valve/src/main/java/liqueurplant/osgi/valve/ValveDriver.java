package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;

import java.util.concurrent.ArrayBlockingQueue;


/**
 * Created by bochalito on 4/12/2016.
 */
@Component(name = "liqueurplant.valve")
public class ValveDriver implements ValveIf {

    private String valveType;
    private ArrayBlockingQueue eventQueue;

    public ValveDriver(String valveType, ArrayBlockingQueue blockingQueue) {
        this.valveType = valveType;
        this.eventQueue = blockingQueue;
    }

    public void open() throws Exception {
        System.out.println("[" + this.valveType + "] ValveDriver opened.");

    }

    public void close() throws Exception {
        System.out.println("[" + this.valveType + "] ValveDriver closed.");
    }
}
