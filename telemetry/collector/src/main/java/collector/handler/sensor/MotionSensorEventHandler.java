package collector.handler.sensor;

import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события от датчика движения
 */
@Component
public class MotionSensorEventHandler implements SensorEventHandler {
    @Override
    public void handle(SensorEvent event) {
        System.out.println("MotionSensorEventHandler");
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
