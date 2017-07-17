package liqueurplant.osgi.silo.controller.api;

import liqueurplant.osgi.silo.controller.api.BaseSignal;

public class StopFillingSignal extends BaseSignal{

  public StopFillingSignal(){}

  public StopFillingSignal(String from, String to) {
    super(from, to);
  }

  public StopFillingSignal(String json) {
    super(json);
  }
}
