package ru.yandex.practicum.telemetry.collector.handler.hub;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.Producer;
import ru.yandex.practicum.telemetry.collector.kafka.TopicType;

import java.time.Instant;

/**
 * Базовый класс для обработчиков событий HubEvent
 * @param <T> Класс, сгенерированный по схеме avro. Представляет record для события хаба.
 */
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final Producer producer;

    @Override
    public void handle(HubEventProto event) {
        if (event.getPayloadCase() != getEventType()) {
            throw new IllegalArgumentException(String.format("Тип события %s не соответствует классу обработчика %s",
                    event.getPayloadCase(), getEventType()));
        }

        T payload = mapToAvro(event);
        Timestamp timestamp = event.getTimestamp();
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()))
                .setPayload(payload)
                .build();

        producer.send(eventAvro, TopicType.HUBS_EVENTS_TOPIC);
    }

    protected abstract T mapToAvro(HubEventProto event);

    public abstract HubEventProto.PayloadCase getEventType();
}
