package liqueurplant.osgi.silo.controller.api;


public class LowLevelReachedSignal extends BaseSignal{

  public LowLevelReachedSignal(){}

  public LowLevelReachedSignal(String from, String to) {
    super(from, to);
  }

  public LowLevelReachedSignal(String json) {
    super(json);
  }
}
