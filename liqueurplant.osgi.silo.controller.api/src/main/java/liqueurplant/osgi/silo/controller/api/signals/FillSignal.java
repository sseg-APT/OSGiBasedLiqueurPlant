package liqueurplant.osgi.silo.controller.api.signals;

import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;


public class FillSignal extends BaseSignal {

  public FillSignal(){}

  public FillSignal(String from, String to) {
    super(from, to);
  }

  public FillSignal(String json) {
    super(json);
  }
}
