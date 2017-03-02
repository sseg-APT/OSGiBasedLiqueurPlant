package liqueurplant.osgi.gpio.test;

import com.pi4j.io.gpio.*;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;


/**
 * Created by bojit on 21-Feb-17.
 */
public class GpioActivator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope","gpio");
        props.put("osgi.command.function", new String[]{"setState", "shutdown"});
        bundleContext.registerService(IGPIOPinOutput.class.getName(), new IGPIOPinOutputImpl(), props);

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

        System.out.println("Goodbye World!");

    }

    private static class IGPIOPinOutputImpl implements IGPIOPinOutput{

        final GpioController gpio = GpioFactory.getInstance();

        @Override
        public void setState(PinState state) {
            final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED", PinState.HIGH);
            pin.setState(true);
        }

        @Override
        public void shutdown() {
            gpio.shutdown();
            gpio.unprovisionPin();
        }
    }

}
