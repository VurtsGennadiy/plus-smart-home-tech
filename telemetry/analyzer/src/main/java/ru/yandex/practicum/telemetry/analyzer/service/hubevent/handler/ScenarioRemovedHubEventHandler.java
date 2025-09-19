package ru.yandex.practicum.telemetry.analyzer.service.hubevent.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.HubEventType;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScenarioRemovedHubEventHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.trace("Scenario Removed Event Handler");

        if (event.getPayload() instanceof ScenarioRemovedEventAvro payload) {
            scenarioRepository.deleteByName(payload.getName());
            log.info("Scenario: '{}' removed", payload.getName());
        } else {
            throw new IllegalArgumentException("Handler expect ScenarioRemovedEvent");
        }
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
