package collector.handler.sensor;

import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события от климатического датчика
 */
@Component
public class ClimateSensorEventHandler implements SensorEventHandler {
    @Override
    public void handle(SensorEvent event) {
        System.out.println("ClimateSensorEventHandler");
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
