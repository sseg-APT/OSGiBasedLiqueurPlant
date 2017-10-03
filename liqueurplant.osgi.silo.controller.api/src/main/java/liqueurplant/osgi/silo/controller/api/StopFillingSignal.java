package liqueurplant.osgi.silo.controller.api;

public class StopFillingSignal extends BaseSignal{

  public StopFillingSignal(){}

  public StopFillingSignal(String from, String to) {
    super(from, to);
  }

  public StopFillingSignal(String json) {
    super(json);
  }
}
