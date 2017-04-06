package liqueurplant.osgi.valve.in.sim;

import liqueurplant.osgi.valve.in.api.InValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bochalito on 23/3/2017.
 */
@Component(immediate = true)
public class InValveDriver implements InValveDriverIf {

    public Logger LOGGER = LoggerFactory.getLogger(InValveDriver.class);


    public InValveDriver() {
    }

    @Activate
    public void activate() {
        LOGGER.info("IN-VALVE activated.");
    }

    @Deactivate
    public void deactivate() {
        LOGGER.info("IN-VALVE deactivated.");
    }

    @Override
    public void open() throws Exception {
        LOGGER.info("IN-VALVE opened.");
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("IN-VALVE closed.");
    }

    @Override
    public String test() {
        return "Test ok.";
    }


}
