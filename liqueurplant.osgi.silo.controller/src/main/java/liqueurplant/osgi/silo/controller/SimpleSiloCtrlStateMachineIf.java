package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;

public interface SimpleSiloCtrlStateMachineIf {

    SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e);

    void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e);
}
