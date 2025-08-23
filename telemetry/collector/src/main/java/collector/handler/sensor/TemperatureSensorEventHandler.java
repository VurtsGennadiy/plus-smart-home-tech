package collector.handler.sensor;

import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события от температурного датчика
 */
@Component
public class TemperatureSensorEventHandler implements SensorEventHandler {
    @Override
    public void handle(SensorEvent event) {
        System.out.println("TemperatureSensorEventHandler");
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
