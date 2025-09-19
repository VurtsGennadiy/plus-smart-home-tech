package ru.yandex.practicum.telemetry.analyzer.service.hubevent.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.HubEventType;

public interface HubEventHandler {
    void handle(HubEventAvro event);

    HubEventType getHubEventType();
}
