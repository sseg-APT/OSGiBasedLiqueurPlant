package liqueurplant.osgi.valve;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import liqueurplant.osgi.valve.api.ValveIf;
import org.osgi.service.component.annotations.Component;

/**
 * Created by bochalito on 4/12/2016.
 */
@Component(name = "liqueurplant.valve")
public class Valve implements ValveIf {

    private String valveType;
    /*
    private GpioController gpio;
    private GpioPinDigitalOutput inValveLed;
    private GpioPinDigitalOutput outValveLed;
    //*/
    public Valve(String valveType) {
        this.valveType = valveType;
        /*
        if(valveType =="IN") {
            gpio = GpioFactory.getInstance();
            inValveLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        }
        if (valveType == "OUT") {
            gpio = GpioFactory.getInstance();
            outValveLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        }
        //*/
    }

    public void open() throws Exception {
        System.out.println("[" + this.valveType + "] Valve opened V1.0.1.");
        /*
        if(valveType =="IN") {
            inValveLed.high();
        }
        if (valveType == "OUT") {
            outValveLed.high();
        }
        //*/
    }

    public void close() throws Exception {
        System.out.println("[" + this.valveType + "] Valve closed V1.0.1.");
        /*
        if(valveType =="IN") {
            inValveLed.low();
        }
        if (valveType == "OUT") {
            outValveLed.low();
        }
        //*/
    }
}
