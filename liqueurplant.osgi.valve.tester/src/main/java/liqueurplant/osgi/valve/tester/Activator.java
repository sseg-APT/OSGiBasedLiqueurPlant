package liqueurplant.osgi.valve.tester;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.*;

/**
 * Created by pBochalis on 3/7/2017.
 */
@Component
public class Activator {

    protected static InValveDriverIf inValve;
    protected static OutValveDriverIf outValve;

    @Activate
    public void activate() {
        try {
            inValve.open();
            outValve.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deactivate
    public void deactivate() {
        try {
            inValve.close();
            outValve.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Reference(
            name = "inValveDriver.service",
            service = InValveDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetInValve"
    )
    private void setInValve(InValveDriverIf inValve) {
        System.out.println("Binding inValve");
        this.inValve = inValve;
    }

    private void unsetInValve(InValveDriverIf inValve) {
        System.out.println("Unbind inValve");
        this.inValve = null;
    }

    @Reference(
            name = "outValveDriver.service",
            service = OutValveDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetOutValve"
    )
    private void setOutValve(OutValveDriverIf outValve) {
        System.out.println("Binding outValve");
        this.outValve = outValve;
    }

    private void unsetOutValve(OutValveDriverIf outValve) {
        System.out.println("Unbinding outValve");
        this.outValve = null;
    }
}
