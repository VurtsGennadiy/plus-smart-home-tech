package ru.yandex.practicum.telemetry.collector.handler.sensor;

import ru.yandex.practicum.telemetry.collector.kafka.Producer;
import ru.yandex.practicum.telemetry.collector.kafka.TopicType;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

/**
 * Базовый класс для обработчиков событий SensorEvent
 * @param <T> Класс, сгенерированный по схеме avro. Представляет record для события сенсора.
 */
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final Producer producer;

    @Override
    public void handle(SensorEventProto event) {
        if (event.getPayloadCase() != getEventType()) {
            throw new IllegalArgumentException(String.format("Тип события %s не соответствует классу обработчика %s",
                    event.getPayloadCase(), getEventType()));
        }

        T payload = mapToAvro(event);
        Timestamp timestamp = event.getTimestamp();
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setTimestamp(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()))
                .setHubId(event.getHubId())
                .setPayload(payload)
                .build();

        producer.send(message, TopicType.SENSORS_EVENTS_TOPIC);
    }

    protected abstract T mapToAvro(SensorEventProto event);

    @Override
    public abstract SensorEventProto.PayloadCase getEventType();
}
