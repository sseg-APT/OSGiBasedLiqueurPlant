package liqueurplant.osgi.plant;

import liqueurplant.osgi.silo.controller.SiloCtrlEvent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bocha on 20/2/2017.
 */
public class LiqueurTypeAGen implements Runnable{

    private ArrayBlockingQueue<SiloCtrlEvent> eventQueue;

    public LiqueurTypeAGen(ArrayBlockingQueue<SiloCtrlEvent> eq){
        eventQueue = eq;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.FILL);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.FILL);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.FILLINGCOMPLETED);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.EMPTY);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.EMPTYINGCOMPLETED);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.EMPTYINGCOMPLETED);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.STOP);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
