package liqueurplant.osgi.valve.out;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        name = "liqueurplant.osgi.valve.out",
        service = liqueurplant.osgi.valve.out.api.OutValveDriverIf.class
)
public class OutValveDriver implements OutValveDriverIf {

    private GpioController gpioController;
    private GpioPinDigitalOutput outValvePin;
    private Logger LOGGER = LoggerFactory.getLogger(OutValveDriver.class);

    public OutValveDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        outValvePin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_05, "OUT-VALVE", PinState.HIGH);
        LOGGER.info("OUT-VALVE activated.");
    }

    @Deactivate
    public void deactivate() {
        unprovisionPins(this.gpioController);
        LOGGER.info("OUT-VALVE deactivated.");
    }

    @Override
    public void open() throws Exception {
        try {
            outValvePin.setState(PinState.LOW);
        } catch (Exception e) {
            LOGGER.error("Exception in open(): " + e.toString());
        }
    }

    @Override
    public void close() throws Exception {
        try {
            outValvePin.setState(PinState.HIGH);
        } catch (Exception e) {
            LOGGER.error("Exception in close(): " + e.toString());
        }
    }

    private void unprovisionPins(GpioController gpioController) {
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
    }
}
