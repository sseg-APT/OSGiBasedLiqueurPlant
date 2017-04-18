package liqueurplant.osgi.valve.in;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        name = "liqueurplant.osgi.valve.in",
        service = liqueurplant.osgi.valve.in.api.InValveDriverIf.class
)
public class InValveDriver implements InValveDriverIf {

    private GpioPinDigitalOutput inValvePin;
    private GpioController gpioController;
    private Logger LOGGER = LoggerFactory.getLogger(InValveDriver.class);

    public InValveDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        inValvePin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "IN-VALVE", PinState.HIGH);
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
            LOGGER.debug("IN-VALVE v1.2 opened.");
            inValvePin.setState(PinState.LOW);
        } catch (Exception e) {
            LOGGER.error("Exception in open(): " + e.toString());
        }
    }

    @Override
    public void close() throws Exception {
        try {
            LOGGER.debug("IN-VALVE v1.2 closed.");
            inValvePin.setState(PinState.HIGH);
        } catch (Exception e) {
            LOGGER.error("Exception in close(): " + e.toString());
        }
    }

    private void unprovisionPins(GpioController gpioController) {
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
    }
}
