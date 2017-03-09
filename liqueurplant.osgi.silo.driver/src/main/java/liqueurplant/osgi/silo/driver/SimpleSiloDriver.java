package liqueurplant.osgi.silo.driver;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.*;

/**
 * Created by bocha on 1/3/2017.
 */
@Component(
        name = "liqueurplant.osgi.silo.driver.SimpleSiloDriver",
        service = liqueurplant.osgi.silo.driver.api.SiloDriverIf.class,
        immediate = true
)
public class SimpleSiloDriver implements SiloDriverIf {

    private GpioController gpioController;
    private GpioPinDigitalInput highLevelReachedSensor;
    private InValveDriverIf inValve;
    private OutValveDriverIf outValve;
    private SiloCtrlIf siloCtrl;

    public SimpleSiloDriver() {
        gpioController = GpioFactory.getInstance();
    }


    @Override
    public void openDriver() {
        System.out.println("Driver started.");
    }

    @Override
    public void closeDriver() {
        System.out.println("Driver stopped.");
    }

    @Override
    public String highLevelReached() throws Exception {

        String[] sensorState = {null};

        try {
            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            highLevelReachedSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
            highLevelReachedSensor.setShutdownOptions(true);

            highLevelReachedSensor.addListener((GpioPinListenerDigital) event -> {
                if(event.getState() == PinState.HIGH){
                    sensorState[0] = Event.HIGH_LEVEL_REACHED.toString();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensorState[0];
    }

    @Override
    public String lowLevelReached() throws Exception {
        String[] sensorState = {null};

        try {
            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            highLevelReachedSensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
            highLevelReachedSensor.setShutdownOptions(true);

            highLevelReachedSensor.addListener((GpioPinListenerDigital) event -> {
                if(event.getState() == PinState.HIGH){
                    sensorState[0] = SimpleSiloDriverEvent.LOW_LEVEL_REACHED.toString();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensorState[0];
    }

    @Reference(
            name = "inValveDriver.service",
            service = InValveDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetInValve"
    )
    private void setInValve(InValveDriverIf inValve) {
        System.out.println("Binding inValve");
        this.inValve = inValve;
    }

    private void unsetInValve(InValveDriverIf inValve) {
        System.out.println("Unbind inValve");
        this.inValve = null;
    }

    @Reference(
            name = "outValveDriver.service",
            service = OutValveDriverIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetOutValve"
    )
    private void setOutValve(OutValveDriverIf outValve) {
        System.out.println("Binding outValve");
        this.outValve = outValve;
    }

    private void unsetOutValve(OutValveDriverIf outValve) {
        System.out.println("Unbinding outValve");
        this.outValve = null;
    }

    /*
    @Reference(
            name = "siloController.service",
            service = SiloCtrlIf.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetSiloCtrl"
    )
    private void setSiloCtrl(SiloCtrlIf siloCtrl) {
        System.out.println("Binding silo controller.");
        this.siloCtrl = siloCtrl;
    }

    private void unsetSiloCtrl(SiloCtrlIf siloCtrl) {
        System.out.println("Unbinding silo controller");
        this.siloCtrl = null;
    }
    //*/

}
