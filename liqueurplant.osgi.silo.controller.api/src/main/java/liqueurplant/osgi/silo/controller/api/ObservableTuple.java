package liqueurplant.osgi.silo.controller.api;

public class ObservableTuple {
    Ctrl2WrapperEvent event;
    SiloCtrlState state;

    public ObservableTuple(Ctrl2WrapperEvent event, SiloCtrlState state){
        this.event = event;
        this.state = state;
    }

    public Ctrl2WrapperEvent getEvent() {
        return event;
    }

    public SiloCtrlState getState() {
        return state;
    }
}
