package liqueurplant.osgi.silo.controller.api.signals;


import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class HighLevelReachedSignal extends BaseSignal{

  public HighLevelReachedSignal(){}

  public HighLevelReachedSignal(String from, String to) {
    super(from, to);
  }

  public HighLevelReachedSignal(String json) {
    super(json);
  }
}
