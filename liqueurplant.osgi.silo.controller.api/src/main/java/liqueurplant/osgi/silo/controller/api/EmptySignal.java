package liqueurplant.osgi.silo.controller.api;




public class EmptySignal extends BaseSignal {

  public EmptySignal(){}

  public EmptySignal(String from, String to) {
    super(from, to);
  }

  public EmptySignal(String json) {
    super(json);
  }
}
