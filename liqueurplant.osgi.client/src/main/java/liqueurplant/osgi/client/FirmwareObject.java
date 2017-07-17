package liqueurplant.osgi.client;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FirmwareObject extends BaseInstanceEnabler {

    public static Logger LOG = LoggerFactory.getLogger(FirmwareObject.class);

    public static int modelId = 5;

    private ExecutorService pool = Executors.newFixedThreadPool(1);
    private String url;
    private File newFirmware;

    private int state = 1;

    private int updateResult = 0;

    @Override
    public ReadResponse read(int resourceid) {
        switch (resourceid) {
            case 3:
                return ReadResponse.success(3, state);
            case 5:
                return ReadResponse.success(5, updateResult);
            default:
                return super.read(resourceid);
        }
    }

    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        switch (resourceid) {
            case 1:
                pool.execute(() -> downloadFirmware((String) value.getValue()));
                return WriteResponse.success();
            default:
                return super.write(resourceid, value);
        }
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 2:
                /*
                if (newFirmware == null) {
                    return ExecuteResponse.badRequest("Url not defined");
                }
                */
                pool.execute(this::update);
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }

    private void setState(int newState) {
        state = newState;
        fireResourcesChange(3);
    }

    private void setUpdateResult(int updateResult) {
        this.updateResult = updateResult;
        fireResourcesChange(5);
    }

    private void downloadFirmware(String url) {
        //If empty set state to idle
        if (url == "") {
            setState(1);
            return;
        }

        //State = downloading
        setState(2);
        try {
            newFirmware = File.createTempFile("update", ".jar");
            LOG.debug("Downloading update to {}", newFirmware.getAbsolutePath());
        } catch (IOException e) {
            LOG.error("Cannot allocate file for download {}", e.toString());
            //Update result = Not enough storage
            setUpdateResult(2);
            setState(3);
            return;
        }
        ReadableByteChannel rbc;
        try {
            URL webResource = new URL(url);
            rbc = Channels.newChannel(webResource.openStream());
        } catch (MalformedURLException e) {
            LOG.error("Malformed url {}", e.toString());
            //Update result = Connection lost
            setUpdateResult(4);
            setState(3);
            return;
        } catch (IOException e) {
            LOG.error("Error opening download stream {}", e.toString());
            //Update result = Connection lost
            setUpdateResult(4);
            setState(3);
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(newFirmware);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            LOG.error("Cannot save firmware file {}", e.toString());
            //Update result = Connection lost
            setUpdateResult(2);
            setState(3);
            return;
        }
        LOG.info("Update downloaded to {}", newFirmware.getAbsolutePath());
        setState(3);
    }

    private void update() {

        try {
            File temp = File.createTempFile("update", ".jar");
            LOG.debug("Downloading update to {}", temp.getAbsolutePath());
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(temp);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            LOG.info("Update downloaded to {}", temp.getAbsolutePath());
        } catch (Exception e) {
            LOG.debug(e.toString());
        }
    }
}
