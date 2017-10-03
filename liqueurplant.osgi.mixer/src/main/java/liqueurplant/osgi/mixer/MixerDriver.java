package liqueurplant.osgi.mixer;

import com.pi4j.io.gpio.*;
import liqueurplant.osgi.mixer.api.MixerDriverIf;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(
        name = "liqueurplant.osgi.mixer",
        service = liqueurplant.osgi.mixer.api.MixerDriverIf.class
)
public class MixerDriver implements MixerDriverIf {

    private GpioController gpioController;
    private GpioPinDigitalOutput mixerPin;
    private Logger LOGGER = LoggerFactory.getLogger(MixerDriver.class);

    public MixerDriver(){
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        mixerPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_06, "MIXER", PinState.HIGH);
        mixerPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT);
        LOGGER.info("MIXER activated.");
    }

    @Deactivate
    public void deactivate() {
        unprovisionPins(this.gpioController);
        LOGGER.info("MIXER deactivated.");
    }

    @Override
    public void start() {
        try {
            mixerPin.setState(PinState.LOW);
        } catch (Exception e) {
            LOGGER.error("Exception in open(): " + e.toString());
        }
    }

    @Override
    public void stop() {
        try {
            mixerPin.setState(PinState.HIGH);
        } catch (Exception e) {
            LOGGER.error("Exception in open(): " + e.toString());
        }
    }

    private void unprovisionPins(GpioController gpioController) {
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
    }


}
