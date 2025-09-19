package ru.yandex.practicum.telemetry.collector.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

/**
 * Интерфейс, объединяющий все обработчики для событий SensorEvent
 */
public interface SensorEventHandler {
    void handle(SensorEventProto event);

    SensorEventProto.PayloadCase getEventType();
}
