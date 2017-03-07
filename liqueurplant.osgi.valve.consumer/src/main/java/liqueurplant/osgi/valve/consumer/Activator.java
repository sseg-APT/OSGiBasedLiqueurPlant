package liqueurplant.osgi.valve.consumer;

import liqueurplant.osgi.valve.in.api.ValveIf;
import org.osgi.service.component.annotations.*;

/**
 * Created by bojit on 03-Mar-17.
 */
@Component
public class Activator {

    private ValveIf valveService;

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
            service = ValveIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "unsetValveIf"
    )
    protected void setValveIf(ValveIf valveService){
        this.valveService = valveService;
    }

    protected void unsetValveIf(ValveIf valveService){
        this.valveService = null;
    }
}
