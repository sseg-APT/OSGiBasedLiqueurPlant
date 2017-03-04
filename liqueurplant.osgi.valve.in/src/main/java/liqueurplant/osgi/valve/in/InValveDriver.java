package liqueurplant.osgi.valve.in;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.valve.api.ValveIf;

/**
 * Created by bojit on 04-Mar-17.
 */
public class InValveDriver implements ValveIf {

    private GpioPinDigitalOutput inValve;
    private GpioController gpioController;


    public InValveDriver() {
        gpioController = GpioFactory.getInstance();
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
