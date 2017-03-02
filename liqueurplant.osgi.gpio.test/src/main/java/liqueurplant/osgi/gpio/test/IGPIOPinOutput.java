package liqueurplant.osgi.gpio.test;

import com.pi4j.io.gpio.PinState;

/**
 * Created by pBochalis on 3/2/2017.
 */
public interface IGPIOPinOutput {

    public void setState();

    public void shutdown();
}
