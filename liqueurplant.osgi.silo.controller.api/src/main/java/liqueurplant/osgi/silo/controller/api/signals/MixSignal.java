package liqueurplant.osgi.silo.controller.api.signals;

import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class MixSignal extends BaseSignal {
    public MixSignal(){}

    public MixSignal(String from, String to) {
        super(from, to);
    }

    public MixSignal(String json) {
        super(json);
    }
}
