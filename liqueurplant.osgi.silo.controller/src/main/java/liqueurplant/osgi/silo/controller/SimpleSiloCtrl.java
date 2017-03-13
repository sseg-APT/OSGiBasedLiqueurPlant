package liqueurplant.osgi.silo.controller;

import liqueurplant.osgi.silo.controller.api.SiloCtrlIf;
import liqueurplant.osgi.silo.driver.api.SiloDriverIf;
import org.osgi.service.component.annotations.*;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

@Component( immediate = true )
public class SimpleSiloCtrl implements SiloCtrlIf, Runnable {

    private ArrayBlockingQueue<SiloDriverIf.Driver2SiloEvent> eventQueue;

    public SimpleSiloCtrl() {
        eventQueue = new ArrayBlockingQueue<>(20);
    }

    @Activate
    public void activate() {
        System.out.println("Silo controller activated.");
    }

    @Override
    public void put2EventQueue(Object o) {
        try {
            eventQueue.put((SiloDriverIf.Driver2SiloEvent) o);
            System.out.println(eventQueue.take().toString());
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
