package liqueurplant.osgi.silo.driver;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;
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


@Component(immediate = true)
public class SimpleSiloDriver implements SiloDriverIf {

    private SiloCtrlIf siloCtrl;
    public static Logger LOGGER = LoggerFactory.getLogger(SimpleSiloDriver.class);
    private final GpioController gpioController;
    private GpioPinDigitalInput highLevelSensor;
    private GpioPinDigitalInput lowLevelSensor;

    public SimpleSiloDriver() {
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        ///*
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

        highLevelSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, "HIGH-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);
        lowLevelSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, "LOW-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);

        highLevelSensor.setShutdownOptions(true);
        lowLevelSensor.setShutdownOptions(true);

        highLevelSensor.addListener((GpioPinListenerDigital) event -> {
            // display pin state on console

            siloCtrl.put2EventQueue(Driver2SiloCtrlEvent.HIGH_LEVEL_REACHED);
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
        });

        lowLevelSensor.addListener((GpioPinListenerDigital) event -> {
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            siloCtrl.put2EventQueue(Driver2SiloCtrlEvent.LOW_LEVEL_REACHED);
        });
        System.out.println("Silo driver activated.");
        //*/
        //siloCtrl.put2EventQueue(Driver2SiloEvent.HIGH_LEVEL_REACHED);
    }

    @Deactivate
    public void deactivate() {

    }


    ///*
    @Reference
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl){
        this.siloCtrl = siloCtrl;
        LOGGER.info(" SILO-CONTROLLER binded.");
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl){
        this.siloCtrl = null;
        LOGGER.info(" SILO-CONTROLLER unbinded.");
    }


    @Override
    public void execute() {

    }
    //*/

}
