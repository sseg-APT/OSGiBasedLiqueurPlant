package liqueurplant.osgi.valve.out;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * Created by pBochalis on 3/6/2017.
 */
@Component(
        name = "liqueurplant.osgi.valve.out",
        service = liqueurplant.osgi.valve.out.api.OutValveDriverIf.class
)
public class OutValveDriver implements OutValveDriverIf {

    private GpioController gpioController;
    private GpioPinDigitalOutput outValve;

    public OutValveDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate(BundleContext context) {
        context.registerService(OutValveDriverIf.class.getName(), new OutValveDriver(), null);
    }

    @Deactivate
    public void deactivate() {

    }

    @Override
    public void open() throws Exception {
        try {
            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            outValve = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "OUT-VALVE", PinState.HIGH);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws Exception {
        try {
            outValve.setState(PinState.LOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}