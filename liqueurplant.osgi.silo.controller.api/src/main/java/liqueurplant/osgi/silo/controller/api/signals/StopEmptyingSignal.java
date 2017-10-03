package liqueurplant.osgi.silo.controller.api.signals;


import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class StopEmptyingSignal extends BaseSignal{

  public StopEmptyingSignal(){}

  public StopEmptyingSignal(String from, String to) {
    super(from, to);
  }

  public StopEmptyingSignal(String json) {
    super(json);
  }
}
