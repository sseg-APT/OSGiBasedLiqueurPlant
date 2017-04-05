package liqueurplant.osgi.client;

import java.io.*;
import java.util.Properties;

/**
 * Created by bochalito on 5/4/2017.
 */
public class Test {

    public Test(){

    }

    public void getFile(String fileName){

        Properties prop = new Properties();
        InputStream is = null;

        try {
            is = this.getClass().getResourceAsStream(fileName);
            if (is != null) {
                prop.load(is);
                System.out.println(prop.getProperty("IP"));
                System.out.println(prop.getProperty("port"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
