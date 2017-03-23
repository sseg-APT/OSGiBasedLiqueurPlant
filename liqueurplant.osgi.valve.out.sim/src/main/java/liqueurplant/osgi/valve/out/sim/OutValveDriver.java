package liqueurplant.osgi.valve.out.sim;

import liqueurplant.osgi.silo.driver.api.Driver2SiloCtrlEvent;
import liqueurplant.osgi.silo.driver.sim.SimpleSiloDriver;
import liqueurplant.osgi.valve.out.api.OutValveDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bochalito on 23/3/2017.
 */
@Component(immediate = true)
public class OutValveDriver implements OutValveDriverIf {
    public Logger LOGGER = LoggerFactory.getLogger(OutValveDriver.class);
    SimpleSiloDriver siloDriver;

    public OutValveDriver() {
        siloDriver = new SimpleSiloDriver();
    }

    @Activate
    public void activate() {
        LOGGER.info("OUT-VALVE activated.");
    }

    @Deactivate
    public void deactivate() {
        LOGGER.info("OUT-VALVE deactivated.");
    }

    @Override
    public void open() throws Exception {
        LOGGER.info("OUT-VALVE opened.");
        siloDriver.put2EventQueue(Driver2SiloCtrlEvent.LOW_LEVEL_REACHED);

    }

    @Override
    public void close() throws Exception {
        LOGGER.info("OUT-VALVE closed.");
    }

}
