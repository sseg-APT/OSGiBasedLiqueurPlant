package liqueurplant.osgi.silo.driver;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Created by bocha on 1/3/2017.
 */


@Component(immediate = true)
public class SimpleSiloDriver implements SiloDriverIf, Runnable {

    private SiloCtrlIf siloCtrl;
    //private final GpioController gpioController;
    //private GpioPinDigitalInput highLevelSensor;
    //private GpioPinDigitalInput lowLevelSensor;

    public SimpleSiloDriver() {
        //gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate() {
        /*
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

        highLevelSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, "HIGH-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);
        lowLevelSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, "LOW-LEVEL-SENSOR", PinPullResistance.PULL_DOWN);

        highLevelSensor.setShutdownOptions(true);
        lowLevelSensor.setShutdownOptions(true);
        //*/
        siloCtrl.put2EventQueue(Driver2SiloEvent.HIGH_LEVEL_REACHED);
    }

    @Deactivate
    public void deactivate() {

    }

    @Override
    public void run() {
        /*
        highLevelSensor.addListener((GpioPinListenerDigital) event -> {
            // display pin state on console
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            siloCtrl.put2EventQueue(Driver2SiloEvent.HIGH_LEVEL_REACHED);
        });

        lowLevelSensor.addListener((GpioPinListenerDigital) event -> {
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            siloCtrl.put2EventQueue(Driver2SiloEvent.LOW_LEVEL_REACHED);
        });
        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //*/
    }

    ///*
    @Reference
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl){
        System.out.println("Binding silo controller service");
        this.siloCtrl = siloCtrl;
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl){
        System.out.println("Unbinding silo controller service.");
        this.siloCtrl = null;
    }
    //*/

}
