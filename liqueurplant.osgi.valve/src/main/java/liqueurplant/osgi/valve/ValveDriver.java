package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.*;


/**
 * Created by bochalito on 4/12/2016.
 */
@Component(
        property = {"foo=valve.v1"}
)
public class ValveDriver implements InValveDriverIf {

    public ValveDriver(){

    }

    @Activate
    public void activate(BundleContext context) throws Exception {
        //context.registerService(InValveDriverIf.class.getName(), new ValveDriver(), null);
    }

    @Deactivate
    public void deactivate(BundleContext context) throws Exception {

    }

    public void open() throws Exception {
        System.out.println("ValveDriver opened.");

    }

    public void close() throws Exception {
        System.out.println("ValveDriver closed.");
    }
}
