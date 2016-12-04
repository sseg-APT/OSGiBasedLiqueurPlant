package liqueurplant.silo;

import liqueurplant.valve.Valve;
import org.osgi.framework.BundleContext;



/**
 * Created by bocha on 28/11/2016.
 */
public class SiloCommand {

    BundleContext bundleContext;
    private Silo testSilo;

    public SiloCommand(BundleContext bundleContext){
        this.bundleContext =bundleContext;
        testSilo = new Silo();

    }

    public void fill(){
        try {
            testSilo.fill();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void empty(){
        try {
            testSilo.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
