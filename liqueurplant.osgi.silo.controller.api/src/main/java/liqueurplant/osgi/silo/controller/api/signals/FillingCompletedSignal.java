package liqueurplant.osgi.silo.controller.api.signals;


import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;


public class FillingCompletedSignal extends BaseSignal{

  public FillingCompletedSignal(){}

  public FillingCompletedSignal(String from, String to) {
    super(from, to);
  }

  public FillingCompletedSignal(String json) {
    super(json);
  }
}
