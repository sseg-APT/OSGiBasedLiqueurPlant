package liqueurplant.silo;


import liqueurplant.silo.api.SiloIf;
import liqueurplant.valve.Valve;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.osgi.service.component.annotations.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component(name = "liqueurplant.silo")
public class Silo extends BaseInstanceEnabler implements SiloIf {

    public static int modelId = 16663;
    private ExecutorService pool = Executors.newFixedThreadPool(2);
    private Valve inValve = new Valve("IN");
    private Valve outValve = new Valve("OUT");

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
            case 1:
                pool.submit(this::fill);
                return ExecuteResponse.success();
            case 2:
                pool.submit(this::empty);
                return ExecuteResponse.success();
            default:
                return super.execute(resourceid, params);
        }
    }


    @Override
    public void fill() {
        try {
            inValve.open();
            System.out.println("Silo filling.");
            Thread.sleep(5000);
            System.out.println("Silo filled.");
            inValve.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void empty() {
        try {
            //outValve.open();
            System.out.println("Silo emptying.");
            Thread.sleep(5000);
            System.out.println("Silo empty.");
            //outValve.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    protected void setInValve(Valve inValve) {
        this.inValve = inValve;
    }

    protected void setOutValve(Valve outValve) {
        this.outValve = outValve;
    }
    //*/
}
