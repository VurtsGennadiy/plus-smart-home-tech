package collector.handler.sensor;

import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события от датчика освещения
 */
@Component
public class LightSensorEventHandler implements SensorEventHandler {
    @Override
    public void handle(SensorEvent event) {
        System.out.println("LightSensorEventHandler");
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
