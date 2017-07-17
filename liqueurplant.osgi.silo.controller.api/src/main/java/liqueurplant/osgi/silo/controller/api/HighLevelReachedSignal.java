package liqueurplant.osgi.silo.controller.api;


import liqueurplant.osgi.silo.controller.api.BaseSignal;

public class HighLevelReachedSignal extends BaseSignal{

  public HighLevelReachedSignal(){}

  public HighLevelReachedSignal(String from, String to) {
    super(from, to);
  }

  public HighLevelReachedSignal(String json) {
    super(json);
  }
}
