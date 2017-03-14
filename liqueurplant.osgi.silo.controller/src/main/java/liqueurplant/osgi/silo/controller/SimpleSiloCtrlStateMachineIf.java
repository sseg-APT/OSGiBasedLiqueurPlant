package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.SiloCtrlEvent;

/**
 * Created by pBochalis on 3/13/2017.
 */
public interface SimpleSiloCtrlStateMachineIf {
    SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e);
    void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e);
}
