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
            name = "invalvedriver.service",
            service = InValveDriverIf.class,
            /* Cardinality (Whether the bundle works with or without service.
            // Mandatory: mandatory and unary
            // At least one: mandatory and multiple
            // Multiple: optional and multiple
            // Optional: optional and unary
            //*/
            cardinality = ReferenceCardinality.MANDATORY,
            /*
            //
            //*/
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetValveIf"
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
