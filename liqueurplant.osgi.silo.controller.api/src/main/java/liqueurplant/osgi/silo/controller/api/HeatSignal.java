package liqueurplant.osgi.silo.controller.api;


public class HeatSignal extends BaseSignal{

    public HeatSignal(){}

    public HeatSignal(String from, String to) {
        super(from, to);
    }

    public HeatSignal(String json) {
        super(json);
    }
}


