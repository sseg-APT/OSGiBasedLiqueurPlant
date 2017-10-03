package liqueurplant.osgi.silo.controller.api.signals;


import liqueurplant.osgi.silo.controller.api.signals.BaseSignal;

public class EmptyingCompletedSignal extends BaseSignal {

  public EmptyingCompletedSignal(){}

  public EmptyingCompletedSignal(String from, String to) {
    super(from, to);
  }

  public EmptyingCompletedSignal(String json) {
    super(json);
  }
}
