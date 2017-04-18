package liqueurplant.osgi.silo.driver.sim;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        name = "liqueurplant.osgi.silo.driver.sim",
        service = liqueurplant.osgi.silo.driver.api.SiloDriverIf.class,
        immediate = true
)
public class SimpleSiloDriver implements SiloDriverIf {

    private SiloCtrlIf siloCtrl;
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSiloDriver.class);

    public SimpleSiloDriver() {

    }

    @Activate
    public void activate() {
        LOGGER.info("SILO-DRIVER activated.");
    }

    @Deactivate
    public void deactivate() {
        LOGGER.info("SILO-DRIVER deactivated.");
    }

    @Reference
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl) {
        this.siloCtrl = siloCtrl;
        LOGGER.info(" SILO-CONTROLLER binded.");
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl) {
        this.siloCtrl = null;
        LOGGER.info(" SILO-CONTROLLER unbinded.");
    }



}
