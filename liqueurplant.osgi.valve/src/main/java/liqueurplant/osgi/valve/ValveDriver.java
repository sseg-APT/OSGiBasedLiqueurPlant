package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;


/**
 * Created by bochalito on 4/12/2016.
 */

public class ValveDriver implements ValveIf {


    public void open() throws Exception {
        System.out.println("ValveDriver opened.");

    }

    public void close() throws Exception {
        System.out.println("ValveDriver closed.");
    }
}
