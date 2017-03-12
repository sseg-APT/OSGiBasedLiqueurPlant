package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.*;

/**
 * Created by bocha on 1/3/2017.
 */


@Component(immediate = true)
public class SimpleSiloDriver implements SiloDriverIf {

    private SiloCtrlIf siloCtrl;

    @Activate
    public void activate() {
        System.out.println("Driver activated.");
    }

    @Deactivate
    public void deactivate() {

    }

    @Override
    public void openDriver() {
        siloCtrl.put2EventQueue("High lever reached");
    }

    @Override
    public void closeDriver() {

    }

    @Reference
    protected void setSiloCtrlIf(SiloCtrlIf siloCtrl){
        System.out.println("Binding silo controller service");
        this.siloCtrl = siloCtrl;
    }

    protected void unsetSiloCtrlIf(SiloCtrlIf siloCtrl){
        System.out.println("Unbinding silo controller service.");
        this.siloCtrl = null;
    }
}
