package liqueurplant.osgi.silo.controller.api;

import liqueurplant.osgi.silo.controller.api.BaseSignal;


public class FillSignal extends BaseSignal {

  public FillSignal(){}

  public FillSignal(String from, String to) {
    super(from, to);
  }

  public FillSignal(String json) {
    super(json);
  }
}
