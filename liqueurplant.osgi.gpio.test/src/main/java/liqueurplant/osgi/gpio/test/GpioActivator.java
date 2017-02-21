package liqueurplant.osgi.gpio.test;

import com.pi4j.io.gpio.*;

import org.apache.log4j.component.scheduler.Scheduler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Reference;

import java.io.Closeable;

/**
 * Created by bojit on 21-Feb-17.
 */
public class GpioActivator implements BundleActivator{




    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("blinky led...");
        BlinkyLed led = new BlinkyLed();
        new Thread(led).start();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

        System.out.println("Goodbye World!");

    }

}
