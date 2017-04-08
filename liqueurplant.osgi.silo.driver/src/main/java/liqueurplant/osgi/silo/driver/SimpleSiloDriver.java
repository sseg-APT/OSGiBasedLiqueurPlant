package liqueurplant.osgi.silo.driver;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.Driver2SiloCtrlEvent;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bocha on 1/3/2017.
 */


@Component(
        name = "liqueurplant.osgi.silo.driver",
        service = liqueurplant.osgi.silo.driver.api.SiloDriverIf.class,
        immediate = true
)
public class SimpleSiloDriver implements SiloDriverIf {

    private SiloCtrlIf siloCtrl;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloDriver.class);
    private final GpioController gpioController;
    private GpioPinDigitalInput highLevelSensor;
    private GpioPinDigitalInput lowLevelSensor;

    public SimpleSiloDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        highLevelSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, "HIGH-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);
        lowLevelSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, "LOW-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);

        highLevelSensor.setShutdownOptions(true);
        lowLevelSensor.setShutdownOptions(true);
        // Check if it works with interrupt or polling.
        highLevelSensor.addListener((GpioPinListenerDigital) event -> {
            if(event.getState() == PinState.HIGH){
                siloCtrl.put2EventQueue(Driver2SiloCtrlEvent.HIGH_LEVEL_REACHED);
            }
        });

        lowLevelSensor.addListener((GpioPinListenerDigital) event -> {
            if (event.getState() == PinState.LOW){
                siloCtrl.put2EventQueue(Driver2SiloCtrlEvent.LOW_LEVEL_REACHED);
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


    @Reference
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl){
        this.siloCtrl = siloCtrl;
        LOGGER.info("SILO-CONTROLLER binded.");
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl){
        this.siloCtrl = null;
        LOGGER.info("SILO-CONTROLLER unbinded.");
    }
}
