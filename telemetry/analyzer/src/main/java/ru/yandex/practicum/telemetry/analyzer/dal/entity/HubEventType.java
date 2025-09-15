package ru.yandex.practicum.telemetry.analyzer.dal.entity;

import ru.yandex.practicum.kafka.telemetry.event.*;

public enum HubEventType {
    DEVICE_ADDED,
    DEVICE_REMOVED,
    SCENARIO_ADDED,
    SCENARIO_REMOVED;

    /**
     * Получает тип события хаба на основе сообщения HubEventAvro поля payload
     */
    public static HubEventType ofHubEventAvro(HubEventAvro hubEventAvro) {
        // switch по instance of (с Java 21)
        return switch (hubEventAvro.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEventAvro -> DEVICE_ADDED;
            case DeviceRemovedEventAvro deviceRemovedEventAvro -> DEVICE_REMOVED;
            case ScenarioAddedEventAvro scenarioAddedEventAvro -> SCENARIO_ADDED;
            case ScenarioRemovedEventAvro scenarioRemovedEventAvro -> SCENARIO_REMOVED;
            case null, default -> throw new IllegalArgumentException("Неизвестный тип события Hub Event");
        };
    }
}
