package liqueurplant.osgi.silo;

import org.osgi.application.Framework;
import org.osgi.framework.*;

import liqueurplant.osgi.valve.Valve;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by bocha on 28/11/2016.
 */
public class SiloCommand {

    BundleContext bundleContext;
    private Silo testSilo;
    private final Object refreshLock = new Object();
    private long refreshTimeout = 5000;

    public SiloCommand(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        testSilo = new Silo();
        testSilo.setInValve(new Valve("IN"));
        testSilo.setOutValve(new Valve("OUT"));

    }

    public void fill() {
        try {
            testSilo.fill();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void empty() {
        try {
            testSilo.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void installValve() {
        File bnd = new File("/home/pi/Downloads/valve-new.jar");
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
        String newBundleSymbolicName = newBundle.getSymbolicName().toString();
        for (Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getSymbolicName().toString().matches(newBundleSymbolicName)) {
                if (bundle.getBundleId() < newBundle.getBundleId()) {
                    System.out.println(bundle.getLocation().toString());
                    String path = bundle.getLocation().toString().replace("file:/","");
                    File file = new File(path);
                    try {
                        file.delete();
                    } catch (Exception x) {
                        // File permission problems are caught here.
                        System.err.println(x);
                    }
                }
                break;
            }
        }
    }

    public void refresh() {
        System.out.println("Refreshing Bundles ... ");

        FrameworkWiring wiring = this.bundleContext.getBundle(0).adapt(FrameworkWiring.class);
        if (wiring != null) {
            synchronized (refreshLock) {
                wiring.refreshBundles(null);
                try {
                    refreshLock.wait(refreshTimeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
