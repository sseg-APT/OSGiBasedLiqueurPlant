package liqueurplant.osgi.silo.controller.api;


import liqueurplant.osgi.silo.controller.api.BaseSignal;


public class FillingCompletedSignal extends BaseSignal{

  public FillingCompletedSignal(){}

  public FillingCompletedSignal(String from, String to) {
    super(from, to);
  }

  public FillingCompletedSignal(String json) {
    super(json);
  }
}
