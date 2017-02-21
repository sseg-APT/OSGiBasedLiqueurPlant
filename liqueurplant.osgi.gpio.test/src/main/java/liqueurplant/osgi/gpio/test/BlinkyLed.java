package liqueurplant.osgi.gpio.test;

import com.pi4j.io.gpio.*;

/**
 * Created by bojit on 21-Feb-17.
 */
public class BlinkyLed implements Runnable{


    @Override
    public void run() {
        try {
            GpioController gpioController = GpioFactory.getInstance();

            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            GpioPinDigitalOutput out = gpioController
                    .provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED1",
                            PinState.LOW);


            out.blink(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
