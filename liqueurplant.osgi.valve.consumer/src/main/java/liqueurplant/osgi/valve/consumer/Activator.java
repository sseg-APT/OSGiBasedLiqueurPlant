package liqueurplant.osgi.valve.consumer;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.service.component.annotations.*;

/**
 * Created by bojit on 03-Mar-17.
 */
@Component
public class Activator {

    private InValveDriverIf valveService;

    @Activate
    public void activate() {
        try {
            valveService.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deactivate
    public void deactivate(){
        try {
            valveService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Reference(
            name = "invalvedriver.service",`
            service = InValveDriverIf.class,
            /* Cardinality (Whether the bundle works with or without service.
            // Mandatory: mandatory and unary 1..1
            // At least one: mandatory and multiple 1..n
            // Multiple: optional and multiple 0..n
            // Optional: optional and unary 0..1
            //*/
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetValveIf",
            target = "(foo=valve.v2)"
    )
    protected void setValveIf(InValveDriverIf valveService){
        System.out.println("Binding valve service");
        this.valveService = valveService;
    }

    protected void unsetValveIf(InValveDriverIf valveService){
        System.out.println("Unbinding valve service");
        this.valveService = null;
    }
}
