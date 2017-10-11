package liqueurplant.osgi.silo.controller.state.machine;

/**
 * Created by pBochalis on 7/17/2017.
 */
import liqueurplant.osgi.silo.controller.api.SMReception;

import java.util.ArrayList;
import java.util.List;

public class Message implements SMReception {

    private Enum id;
    private List<Object> arguments;

    public Message(Enum id, List<Object> arguments)
    {
        this.id = id;
        this.arguments = arguments;
    }

    public Message(Enum id)
    {
        this.id = id;
        arguments = new ArrayList<>(3);
    }

    public void addArgument(Object arg)
    {
        arguments.add(arg);
    }

    public <T> T getArgument(int index, Class<T> type){
        return type.cast(arguments.get(index));
    }

    public Enum getId()
    {
        return id;
    }


}