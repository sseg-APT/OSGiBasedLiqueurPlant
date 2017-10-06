package liqueurplant.osgi.heater.api;

public interface HeaterDriverIf {

    void start();

    void stop();

    void heat2temp(float temperature);

    float getTemperature();

}
