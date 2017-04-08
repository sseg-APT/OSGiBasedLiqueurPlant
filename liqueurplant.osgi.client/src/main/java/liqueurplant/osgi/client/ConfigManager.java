package liqueurplant.osgi.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by bojit on 08-Apr-17.
 */
public class ConfigManager {

    public ConfigManager() {

    }

    public void getFile(String fileName) {

        Properties props = new Properties();
        InputStream inputStream = null;
        inputStream = this.getClass().getResourceAsStream(fileName);
        if (inputStream != null) {
            try {
                props.load(inputStream);
                System.out.println("IP: " + props.getProperty("IP"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
