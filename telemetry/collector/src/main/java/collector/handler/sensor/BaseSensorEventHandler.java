package collector.handler.sensor;

import collector.kafka.Producer;
import collector.kafka.TopicType;
import collector.model.sensor.SensorEvent;
import collector.model.sensor.SensorEventType;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

/**
 * Базовый класс для обработчиков событий SensorEvent
 * @param <T> Класс, сгенерированный по схеме avro. Представляет record для события сенсора.
 */
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final Producer producer;

    @Override
    public void handle(SensorEvent event) {
        T payload = mapToAvro(event);

        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setHubId(event.getHubId())
                .setPayload(payload)
                .build();

        producer.send(message, TopicType.SENSORS_EVENTS_TOPIC);
    }

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public abstract SensorEventType getEventType();
}
