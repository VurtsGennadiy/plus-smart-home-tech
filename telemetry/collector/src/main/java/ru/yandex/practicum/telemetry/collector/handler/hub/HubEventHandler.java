package ru.yandex.practicum.telemetry.collector.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

/**
 * Интерфейс, объединяющий все обработчики для событий HubEvent
 */
public interface HubEventHandler {
    void handle(HubEventProto event);

    HubEventProto.PayloadCase getEventType();
}
