package liqueurplant.osgi.heater;


import com.pi4j.io.gpio.*;
import com.pi4j.io.serial.*;
import liqueurplant.osgi.heater.api.HeaterDriverIf;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(
        name = "liqueurplant.osgi.heater",
        service = liqueurplant.osgi.heater.api.HeaterDriverIf.class
)
public class HeaterDriver implements HeaterDriverIf,SerialDataEventListener {

    final Serial serial = SerialFactory.createInstance();
    private boolean isHeating = false;
    private GpioController gpioController;
    private GpioPinDigitalOutput heaterPin;
    private Logger LOGGER = LoggerFactory.getLogger(HeaterDriver.class);

    private float currentTemperature = 25.0f;
    private float targetTemperature;

    public HeaterDriver(){
        gpioController = GpioFactory.getInstance();
    }

    @Activate
    public void activate(){
        heaterPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_10, "HEATER", PinState.HIGH);
        heaterPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_OUTPUT);
        LOGGER.info("HEATER activated.");

        serial.addListener(this);
        try {
            SerialConfig config = new SerialConfig();
            config.device(SerialPort.getDefaultPort())
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            serial.open(config);

        } catch(IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Deactivate
    public void deactivate(){
        unprovisionPins(this.gpioController);
        LOGGER.info("HEATER deactivated.");

    }

    @Override
    public void start() {
        try {
            heaterPin.setState(PinState.LOW);
            isHeating = true;
        } catch (Exception e) {
            LOGGER.error("Exception in open(): " + e.toString());
        }
    }

    @Override
    public void stop() {
        try {
            heaterPin.setState(PinState.HIGH);
            isHeating = false;
        } catch (Exception e) {
            LOGGER.error("Exception in open(): " + e.toString());
        }
    }

    @Override
    public void heat2temp(float temperature) {
        targetTemperature = temperature;
        start();

    }

    @Override
    public float getTemperature() {
        return currentTemperature;
    }


    private void unprovisionPins(GpioController gpioController) {
        while (!gpioController.getProvisionedPins().isEmpty())
            gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());
    }

    @Override
    public void dataReceived(SerialDataEvent serialDataEvent) {
        String receivedString;
        try {
            receivedString = serialDataEvent.getAsciiString();
            if(receivedString.startsWith("T=") && receivedString.endsWith("\n")){
                float temperature=Float.parseFloat(receivedString.substring(2,receivedString.length()-1));
                currentTemperature = temperature;

                if((currentTemperature == targetTemperature) && isHeating){
                    stop();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
