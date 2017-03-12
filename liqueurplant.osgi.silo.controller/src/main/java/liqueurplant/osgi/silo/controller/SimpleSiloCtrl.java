package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import org.osgi.service.component.annotations.*;

import java.util.concurrent.ArrayBlockingQueue;

@Component( immediate = true )
public class SimpleSiloCtrl implements SiloCtrlIf, Runnable {

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
            System.out.println(eventQueue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
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
