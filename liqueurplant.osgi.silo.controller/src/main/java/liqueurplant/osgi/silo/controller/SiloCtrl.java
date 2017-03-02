package liqueurplant.osgi.silo.controller;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bocha on 1/3/2017.
 */
public abstract class SiloCtrl {
    abstract public ArrayBlockingQueue<SiloCtrlEvent> getEventQueue();
}
