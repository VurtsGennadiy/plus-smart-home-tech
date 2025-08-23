package collector.handler.sensor;

import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события от датчика переключателя
 */
@Component
public class SwitchSensorEventHandler implements SensorEventHandler {
    @Override
    public void handle(SensorEvent event) {
        System.out.println("SwitchSensorEventHandler");
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
