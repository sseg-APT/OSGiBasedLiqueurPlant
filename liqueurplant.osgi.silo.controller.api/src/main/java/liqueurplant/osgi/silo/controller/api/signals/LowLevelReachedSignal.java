package liqueurplant.osgi.silo.controller.api.signals;


import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class LowLevelReachedSignal extends BaseSignal{

  public LowLevelReachedSignal(){}

  public LowLevelReachedSignal(String from, String to) {
    super(from, to);
  }

  public LowLevelReachedSignal(String json) {
    super(json);
  }
}
