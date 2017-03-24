package liqueurplant.osgi.valve.in;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public Logger LOGGER = LoggerFactory.getLogger(InValveDriver.class);


    public InValveDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        inValve = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "IN-VALVE", PinState.HIGH);
        LOGGER.info("IN-VALVE activated.");
    }

    @Deactivate
    public void deactivate() {
        unprovisionPins(this.gpioController);
        LOGGER.info("IN-VALVE deactivated.");
    }

    @Override
    public void open() throws Exception {
        try {
            inValve.setState(PinState.LOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        try {
            inValve.setState(PinState.HIGH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unprovisionPins(GpioController gpioController) {
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
    }
}
