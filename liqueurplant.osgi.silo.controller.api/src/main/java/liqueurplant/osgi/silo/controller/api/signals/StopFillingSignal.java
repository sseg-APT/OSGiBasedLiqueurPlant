package liqueurplant.osgi.silo.controller.api.signals;

import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class StopFillingSignal extends BaseSignal{

  public StopFillingSignal(){}

  public StopFillingSignal(String from, String to) {
    super(from, to);
  }

  public StopFillingSignal(String json) {
    super(json);
  }
}
