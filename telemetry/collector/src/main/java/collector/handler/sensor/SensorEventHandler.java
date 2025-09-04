package collector.handler.sensor;

import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;

/**
 * Интерфейс, объединяющий все обработчики для событий SensorEvent
 */
public interface SensorEventHandler {
    void handle(SensorEvent event);

    SensorEventType getEventType();
}
