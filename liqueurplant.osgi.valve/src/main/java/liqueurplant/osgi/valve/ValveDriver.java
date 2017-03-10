package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.*;


/**
 * Created by bochalito on 4/12/2016.
 */
@Component
public class ValveDriver implements InValveDriverIf {

    public ValveDriver(){

    }

    public void open() throws Exception {
        System.out.println("ValveDriver opened.");

    }

    public void close() throws Exception {
        System.out.println("ValveDriver closed.");
    }
}
