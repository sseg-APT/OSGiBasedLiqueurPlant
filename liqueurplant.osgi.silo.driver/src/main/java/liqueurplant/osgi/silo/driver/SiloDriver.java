package liqueurplant.osgi.silo.driver;

import liqueurplant.osgi.silo.controller.SiloCtrlEvent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by bocha on 1/3/2017.
 */
public class SiloDriver {
    public ArrayBlockingQueue<SiloDriverEvent> itsEq;
    protected ArrayBlockingQueue<SiloCtrlEvent> itsCtrlEq;
    SiloDriverEvent curEvent;
}
