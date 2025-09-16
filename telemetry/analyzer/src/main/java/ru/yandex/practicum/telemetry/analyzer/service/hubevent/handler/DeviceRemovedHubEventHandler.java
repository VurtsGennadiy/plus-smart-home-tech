package ru.yandex.practicum.telemetry.analyzer.service.hubevent.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.HubEventType;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Sensor;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeviceRemovedHubEventHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.trace("Device Removed Hub Event Handler");

        if (event.getPayload() instanceof DeviceRemovedEventAvro payload) {
            Sensor sensor = Sensor.builder()
                    .hubId(event.getHubId())
                    .id(payload.getId())
                    .build();

            // удаляем также все сценарии, связанные с этим сенсором
            Set<Scenario> scenarios = scenarioRepository.findScenariosBySensorId(sensor.getId());
            scenarioRepository.deleteAll(scenarios);

            log.info("Removed sensor: '{}' and scenarios: {} from hub: '{}'",
                    sensor.getId(),
                    scenarios.stream().map(Scenario::getName).toList(),
                    sensor.getHubId());

            sensorRepository.delete(sensor);
        } else {
            throw new IllegalArgumentException("Handler expect DeviceRemovedEvent");
        }
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
