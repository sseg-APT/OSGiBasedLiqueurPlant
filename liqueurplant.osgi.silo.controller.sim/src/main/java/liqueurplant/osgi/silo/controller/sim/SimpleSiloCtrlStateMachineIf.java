package liqueurplant.osgi.silo.controller.sim;

import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;

public interface SimpleSiloCtrlStateMachineIf {

    SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e);

    void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e);
}
