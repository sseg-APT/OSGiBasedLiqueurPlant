package liqueurplant.osgi.valve.in;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * Created by bojit on 04-Mar-17.
 */
@Component(
        name = "liqueurplant.osgi.valve.in",
        service = liqueurplant.osgi.valve.in.api.InValveDriverIf.class
)
public class InValveDriver implements InValveDriverIf {

    private GpioPinDigitalOutput inValve;
    private GpioController gpioController;

    public InValveDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate(BundleContext context) {
        context.registerService(InValveDriverIf.class.getName(), new InValveDriver(), null);
    }

    @Deactivate
    public void deactivate() {

    }

    @Override
    public void open() throws Exception {
        try {
            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            inValve = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "IN-VALVE", PinState.HIGH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        try {
            inValve.setState(PinState.LOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
