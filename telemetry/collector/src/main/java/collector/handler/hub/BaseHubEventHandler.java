package collector.handler.hub;

import collector.kafka.Producer;
import collector.kafka.TopicType;
import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

/**
 * Базовый класс для обработчиков событий HubEvent
 * @param <T> Класс, сгенерированный по схеме avro. Представляет record для события хаба.
 */
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final Producer producer;

    @Override
    public void handle(HubEvent event) {
        T payload = mapToAvro(event);

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        producer.send(eventAvro, TopicType.HUBS_EVENTS_TOPIC);
    }

    protected abstract T mapToAvro(HubEvent event);

    public abstract HubEventType getEventType();
}
