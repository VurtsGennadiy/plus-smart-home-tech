package ru.yandex.practicum.telemetry.analyzer.service.hubevent.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.HubEventType;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Sensor;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeviceAddedHubEventHandler implements HubEventHandler {
    private final SensorRepository repository;

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.trace("Device Added Hub Event Handler");

        if (event.getPayload() instanceof DeviceAddedEventAvro payload) {
            Sensor sensor = Sensor.builder()
                    .hubId(event.getHubId())
                    .id(payload.getId())
                    .build();

            log.info("Save sensor: '{}' from hub: '{}'", sensor.getId(), sensor.getHubId());
            repository.save(sensor);
        } else {
            throw new IllegalArgumentException("Handler expect DeviceAddedEvent");
        }
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.DEVICE_ADDED;
    }
}
