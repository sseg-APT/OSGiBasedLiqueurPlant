package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;

/**
 * Created by bochalito on 4/12/2016.
 */
@Component(name = "liqueurplant.valve")
public class Valve implements ValveIf {

    private String valveType;

    public Valve(String valveType) {
        this.valveType = valveType;
    }

    public void open() throws Exception {
        System.out.println("[" + this.valveType + "] Valve opened.");
        while(true);
    }

    public void close() throws Exception {
        System.out.println("[" + this.valveType + "] Valve closed.");
    }
}
