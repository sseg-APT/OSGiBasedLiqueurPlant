package liqueurplant.osgi.silo.controller.api;

/**
 * Created by bochalito on 6/10/2017.
 */
public class HeatingCompletedSignal  extends BaseSignal{
    public HeatingCompletedSignal(){}

    public HeatingCompletedSignal(String from, String to) {
        super(from, to);
    }

    public HeatingCompletedSignal(String json) {
        super(json);
    }
}
