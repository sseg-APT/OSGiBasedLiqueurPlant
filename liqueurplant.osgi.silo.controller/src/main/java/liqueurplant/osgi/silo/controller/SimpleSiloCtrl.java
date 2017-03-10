package liqueurplant.osgi.silo.controller;

//import liqueurplant.osgi.plant.LiqueurPlant;
import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
//import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.*;

import java.util.concurrent.ArrayBlockingQueue;

@Component
public class SimpleSiloCtrl extends Thread implements SiloCtrlIf {

    private ArrayBlockingQueue<String> eventQueue;

    public SimpleSiloCtrl() {
        eventQueue = new ArrayBlockingQueue<String>(20);

    }

    @Activate
    public void activate() {
        System.out.println("Silo controller activated.");
    }

    @Override
    public void put2EventQueue(String event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true){
            if(!eventQueue.isEmpty()){
                try {
                    System.out.println(eventQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
