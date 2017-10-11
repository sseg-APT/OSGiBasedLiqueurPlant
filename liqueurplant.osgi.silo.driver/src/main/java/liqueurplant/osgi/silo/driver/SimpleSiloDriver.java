package liqueurplant.osgi.silo.driver;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.controller.api.HighLevelReachedSignal;
import liqueurplant.osgi.silo.controller.api.LowLevelReachedSignal;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        name = "liqueurplant.osgi.silo.driver",
        immediate = true
)
public class SimpleSiloDriver {

    private SiloCtrlIf siloCtrl;
    private final GpioController gpioController;
    private GpioPinDigitalInput lowLevelSensorPin;
    private GpioPinDigitalInput highLevelSensorPin;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloDriver.class);

    public SimpleSiloDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {

        highLevelSensorPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, "HIGH-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);
        lowLevelSensorPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, "LOW-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);

        highLevelSensorPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT);
        lowLevelSensorPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT);


        highLevelSensorPin.addListener((GpioPinListenerDigital) event -> {
            if (event.getState() == PinState.HIGH) {
                siloCtrl.put2MsgQueue(new HighLevelReachedSignal());
            }
        });

        lowLevelSensorPin.addListener((GpioPinListenerDigital) event -> {
            if (event.getState() == PinState.LOW) {
                siloCtrl.put2MsgQueue(new LowLevelReachedSignal());
            }
        });
        LOGGER.info("SILO-DRIVER activated.");
    }

    @Deactivate
    public void deactivate() {
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
        LOGGER.info("SILO-DRIVER deactivated.");
    }

    @Reference(
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.OPTIONAL
    )
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOGGER.info("SILO-CONTROLLER binded.");
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl) {
        this.siloCtrl = null;
        LOGGER.info("SILO-CONTROLLER unbinded.");
    }
}