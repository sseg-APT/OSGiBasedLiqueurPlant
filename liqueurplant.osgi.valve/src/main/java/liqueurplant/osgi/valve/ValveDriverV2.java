package liqueurplant.osgi.valve;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.service.component.annotations.Component;

/**
 * Created by pBochalis on 3/8/2017.
 */
@Component(
        property = {"foo=valve.v2"}
)
public class ValveDriverV2 implements InValveDriverIf{

    public ValveDriverV2() {

    }

    @Override
    public void open() throws Exception {
        System.out.println("ValveDriverV2 opened.");
    }

    @Override
    public void close() throws Exception {
        System.out.println("ValveDriverV2 closed.");
    }
}
