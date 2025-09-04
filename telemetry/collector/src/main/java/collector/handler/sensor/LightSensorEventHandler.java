package collector.handler.sensor;

import collector.kafka.Producer;
import collector.model.sensor.LightSensorEvent;
import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

/**
 * Обработчик события от датчика освещения
 */
@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    public LightSensorEventHandler(Producer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensorEvent lightSensorEvent = (LightSensorEvent) event;

        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
