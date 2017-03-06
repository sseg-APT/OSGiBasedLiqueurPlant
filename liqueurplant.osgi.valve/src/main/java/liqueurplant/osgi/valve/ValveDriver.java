package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.in.api.ValveIf;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;


/**
 * Created by bochalito on 4/12/2016.
 */
@Component
public class ValveDriver implements ValveIf {

    public ValveDriver(){

    }

    public void open() throws Exception {
        System.out.println("ValveDriver opened.");

    }

    public void close() throws Exception {
        System.out.println("ValveDriver closed.");
    }
}
