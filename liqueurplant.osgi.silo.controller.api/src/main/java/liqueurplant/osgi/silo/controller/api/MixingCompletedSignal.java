package liqueurplant.osgi.silo.controller.api;


import liqueurplant.osgi.silo.controller.api.BaseSignal;

public class MixingCompletedSignal extends BaseSignal{

  public MixingCompletedSignal(){}

  public MixingCompletedSignal(String from, String to) {
    super(from, to);
  }

  public MixingCompletedSignal(String json) {
    super(json);
  }
}
