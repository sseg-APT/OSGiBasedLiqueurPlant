package liqueurplant.osgi.gpio.test;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.system.SystemInfo;
import org.apache.log4j.component.scheduler.Scheduler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Reference;

import java.io.Closeable;
import java.util.Hashtable;


/**
 * Created by bojit on 21-Feb-17.
 */
public class GpioActivator implements BundleActivator {

    private GpioPinDigitalOutput out;
    private GpioController gpioController;
    GpioPinDigitalInput myButton;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope","gpio");
        props.put("osgi.command.function", new String[]{"setState", "shutdown"});
        bundleContext.registerService(IGPIOPinOutput.class.getName(), new IGPIOPinOutputImpl(), props);
        //System.out.println(SystemInfo.getBoardType().name()
        //        + " " + SystemInfo.getSerial());
        ///*
        try {
            gpioController = GpioFactory.getInstance();

            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            out = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LED1", PinState.HIGH);
            myButton = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02,PinPullResistance.PULL_DOWN);
            myButton.setShutdownOptions(true);
            myButton.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // display pin state on console
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //*/
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

        //*
        try {
            while (!gpioController.getProvisionedPins().isEmpty())
                gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

            out = gpioController
                    .provisionDigitalOutputPin(RaspiPin.GPIO_03, "LED1",
                            PinState.LOW);

            System.out.println("Goodbye World!");
            gpioController.unprovisionPin(out);
            gpioController.unprovisionPin(myButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //*/

    }


    private static class IGPIOPinOutputImpl implements IGPIOPinOutput{

        //final GpioController gpio = GpioFactory.getInstance();

        @Override
        public void setState() {
            System.out.println("Set state");
            //final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED", PinState.HIGH);
            //pin.setState(true);
        }

        @Override
        public void shutdown() {
            System.out.println("Shutdown");
            //gpio.shutdown();
            //gpio.unprovisionPin();
        }
    }

}
