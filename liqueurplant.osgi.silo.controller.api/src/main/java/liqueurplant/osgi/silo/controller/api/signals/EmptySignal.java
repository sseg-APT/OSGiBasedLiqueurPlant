package liqueurplant.osgi.silo.controller.api.signals;


import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class EmptySignal extends BaseSignal {

  public EmptySignal(){}

  public EmptySignal(String from, String to) {
    super(from, to);
  }

  public EmptySignal(String json) {
    super(json);
  }
}
