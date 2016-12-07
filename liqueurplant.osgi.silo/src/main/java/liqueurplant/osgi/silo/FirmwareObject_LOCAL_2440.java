package liqueurplant.osgi.silo;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.osgi.application.Framework;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.namespace.HostNamespace;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FirmwareObject extends BaseInstanceEnabler{

    public static Logger LOG = LoggerFactory.getLogger(FirmwareObject.class);

    public static int modelId = 5;

    private ExecutorService pool = Executors.newFixedThreadPool(1);
    private String url;

    @Override
    public ReadResponse read(int resourceid) {
        switch (resourceid) {
            default:
                return super.read(resourceid);
        }
    }

    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        switch (resourceid) {
            case 1:
                url = (String) value.getValue();
                return WriteResponse.success();
            default:
                return super.write(resourceid, value);
        }
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 2:
                if (url == null) {
                    return ExecuteResponse.badRequest("Url not defined");
                }
                pool.execute( this::update);
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }

    private void update() {

        try {
            /*
            File temp = File.createTempFile("update", ".jar");
            LOG.debug("Downloading update to {}", temp.getAbsolutePath());
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(temp);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            LOG.info("Update downloaded to {}", temp.getAbsolutePath());
            */
            File bnd = new File("C:/Users/bocha/Desktop/valve-new.jar");
            LOG.info(bnd.getAbsolutePath().toString());
            //LOG.info((String) bnd.getAbsolutePath().toString().replace("\\","/"));
            BundleContext bundleContext = SiloActivator.getBundleContext();
            Bundle newBundle = bundleContext.installBundle("file:" + bnd.getAbsolutePath().toString().replace("\\","/"));
            newBundle.start();
            
            //bundleContext.getBundle().update(); //updateValves only not whole silo!!!
        }
        catch (Exception e) {

        }
        //catch (IOException e) {
        //    LOG.error(e.toString());
        //}
        //catch (BundleException e) {
        //    e.printStackTrace();
        //}

    }

}
