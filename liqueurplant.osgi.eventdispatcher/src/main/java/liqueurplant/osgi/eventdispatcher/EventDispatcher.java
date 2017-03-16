package liqueurplant.osgi.eventdispatcher;

import liqueurplant.osgi.eventdispatcher.api.EventDispatcherIf;
import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.controller.api.SiloCtrlState;
import org.osgi.annotation.versioning.ConsumerType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pBochalis on 3/16/2017.
 */
@Component(immediate = true)
public class EventDispatcher implements EventDispatcherIf {

    private Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

    public EventDispatcher(){

    }

    @Activate
    public void activate() {
        LOGGER.info("EVENT DISPATCHER activated.");
    }


    @Deactivate
    public void deactivate() {
        LOGGER.info("EVENT DISPATCHER deactivated.");
    }

    @Override
    public void setState(SiloCtrlEvent event, SiloCtrlState state) {

    }
}
