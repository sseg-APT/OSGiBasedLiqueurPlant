package liqueurplant.osgi.silo.controller;

/**
 * Created by bocha on 18/2/2017.
 */
public interface SimpleSiloCtrlStateMachineIf {

    SimpleSiloCtrlState processEvent(SimpleSiloCtrl ctrl, SiloCtrlEvent e);
    void performActions(SimpleSiloCtrl ctrl, SiloCtrlEvent e);

}
