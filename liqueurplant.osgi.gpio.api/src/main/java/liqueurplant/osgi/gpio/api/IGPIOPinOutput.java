package liqueurplant.osgi.gpio.api;

/**
 * Created by bocha on 21/2/2017.
 */

public interface IGPIOPinOutput extends IGPIOPin {

    public boolean getState();

    public void setState(boolean value);

    public boolean toggle();

    public void pulse(long duration, boolean pulseState);

    public void blink(long delay, long duration, boolean blinkState);

}