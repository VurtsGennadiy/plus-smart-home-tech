package ru.yandex.practicum.telemetry.analyzer.service.hubevent.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Action;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.HubEventType;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScenarioAddedHubEventHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.trace("Scenario Added Hub Event Handler");

        if (event.getPayload() instanceof ScenarioAddedEventAvro payload) {
            Map<String, Condition> conditions = payload.getConditions().stream()
                    .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId, Condition::ofScenarioConditionAvro));
            conditionRepository.saveAll(conditions.values());

            Map<String, Action> actions = payload.getActions().stream()
                    .collect(Collectors.toMap(DeviceActionAvro::getSensorId, Action::ofDeviceActionAvro));
            actionRepository.saveAll(actions.values());

            Scenario scenario = Scenario.builder()
                    .hubId(event.getHubId())
                    .name(payload.getName())
                    .conditions(conditions)
                    .actions(actions)
                    .build();
            scenarioRepository.save(scenario);

            log.info("Scenario: '{}' saved", scenario.getName());
        } else {
            throw new IllegalArgumentException("Handler expect ScenarioAddedEvent");
        }
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
