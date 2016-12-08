package liqueurplant.osgi.silo;

import org.osgi.framework.*;

import liqueurplant.osgi.valve.Valve;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloCommand {

    BundleContext bundleContext;
    private Silo testSilo;

    public SiloCommand(BundleContext bundleContext){
        this.bundleContext =bundleContext;
        testSilo = new Silo();
        testSilo.setInValve(new Valve("IN"));
        testSilo.setOutValve(new Valve("OUT"));

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

    public void updateValve(){
        testSilo.updateInValve(new Valve("IN"));
        testSilo.updateOutValve(new Valve("OUT"));
    }

    public void installValve() {
        File bnd = new File("C:/Users/bojit/Desktop/valve-new.jar");
        //LOG.info((String) bnd.getAbsolutePath().toString().replace("\\","/"));
        Bundle newBundle = null;
        try {
            newBundle = bundleContext.installBundle("file:" + bnd.getAbsolutePath().toString().replace("\\", "/"));
        } catch (BundleException e) {
            e.printStackTrace();
        }
        BundleStartLevel startLevel = newBundle.adapt(BundleStartLevel.class);
        startLevel.setStartLevel(1);
        try {
            newBundle.start(1);
        } catch (BundleException e) {
            e.printStackTrace();
        }
        Bundle systemBundle = bundleContext.getBundle(0);
        FrameworkWiring fw = systemBundle.adapt(FrameworkWiring.class);
        //fw.refreshBundles( null, new FrameworkListener(){
        //    public void frameworkEvent(FrameworkEvent ev) {
        //        System.out.println("Refresh finished");
        //    }
        //});
        System.out.println(systemBundle.getSymbolicName());
        BundleWiring wiring = bundleContext.getBundle().adapt(BundleWiring.class);
        for(BundleWire wire : wiring.getRequiredWires("osgi.wiring.package")){
            String pack = (String) wire.getCapability().getAttributes().get("osgi.wiring.package");
            Bundle bundle = wire.getProviderWiring().getBundle();
            System.out.println(pack + " " + bundle.getLocation());
        }
        Set<BundleWiring> result = new HashSet<BundleWiring>();
        BundleWiring wA = bundleContext.getBundle().adapt( BundleWiring.class );
        for ( BundleWire wire : wA.getProvidedWires("osgi.wiring.host")) {
            result.add( wire.getRequirerWiring() );
            System.out.println(wire.getRequirerWiring().toString());
        }
    }

    public void refresh(){
        Bundle systemBundle = bundleContext.getBundle(0);
        FrameworkWiring fw = systemBundle.adapt(FrameworkWiring.class);
        fw.resolveBundles(null);
        fw.refreshBundles( null, new FrameworkListener(){
            public void frameworkEvent(FrameworkEvent ev) {
                System.out.println(ev.toString());
            }
        });

    }

}
