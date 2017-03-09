package liqueurplant.osgi.silo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bojit on 03-Mar-17.
 */
public class SiloProcess implements Runnable {

    private ArrayBlockingQueue<SiloCtrlEvent> eventQueue;

    public SiloProcess(ArrayBlockingQueue<SiloCtrlEvent> eq){
        eventQueue = eq;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.FILL);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.FILL);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.HIGH_LEVEL_REACHED);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.EMPTY);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.LOW_LEVEL_REACHED);
            Thread.sleep(2000);
            eventQueue.put(SiloCtrlEvent.STOP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
