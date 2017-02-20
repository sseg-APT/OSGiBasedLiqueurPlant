package liqueurplant.osgi.silo;

/**
 * Created by bocha on 18/2/2017.
 */
public interface SiloCtrlStateMachineIf {

    SiloCtrlState processEvent(SiloCtrlState scState, SiloCtrlEvent e);
    void performActions();

}
