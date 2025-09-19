package ru.yandex.practicum.telemetry.analyzer.service.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {
    private final ScenarioRepository scenarioRepository;

    /**
     * Выборка сценариев, условия исполнения для которых, удовлетворяют полученному снэпшоту.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Scenario> findMatchingScenarios(SensorsSnapshotAvro snapshot) {
        log.trace("Handle snapshot {}", snapshot);
        List<Scenario> scenarios = scenarioRepository.findScenariosByHubId(snapshot.getHubId());
        return scenarios.stream()
                .filter(scenario -> checkConditions(scenario, snapshot))
                .toList();
    }

    /**
     * Проверка выполнения всех условий для одного сценария
     */
    private boolean checkConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        log.trace("Check conditions for scenario id: {}", scenario.getId());
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        Map<String, Condition> conditions = scenario.getConditions();

        return conditions.entrySet().stream()
                .allMatch(entry -> {
                    SensorStateAvro sensorState = sensorsState.get(entry.getKey());
                    if (sensorState == null) {
                        log.warn("Snapshot does not contain the state of the sensor '{}'", entry.getKey());
                        return false;
                    }
                    return checkCondition(entry.getValue(), sensorState);
                });

    }

    /**
     * Проверка выполнения одного условия
     */
    private boolean checkCondition(Condition condition, SensorStateAvro sensorState) {
        ConditionTypeAvro type = condition.getType();
        ConditionOperationAvro operation = condition.getOperation();
        Integer value = condition.getValue();
        log.trace("Check condition id: '{}', operation: '{}', type: '{}', value: '{}'",
                condition.getId(), type, operation, value);

        return switch (sensorState.getData()) {
            case ClimateSensorAvro climateSensor -> switch (type) {
                case TEMPERATURE -> conditionOperationResolve(operation, climateSensor.getTemperatureC(), value);
                case CO2LEVEL -> conditionOperationResolve(operation, climateSensor.getCo2Level(), value);
                case HUMIDITY -> conditionOperationResolve(operation, climateSensor.getHumidity(), value);
                case null, default -> false;
            };

            case LightSensorAvro lightSensor -> {
                if (ConditionTypeAvro.LUMINOSITY.equals(type)) {
                    yield conditionOperationResolve(operation, lightSensor.getLuminosity(), value);
                } else yield false;
            }

            case MotionSensorAvro motionSensor -> {
                if (ConditionTypeAvro.MOTION.equals(type)) {
                    yield motionSensor.getMotion();
                } else yield false;
            }

            case SwitchSensorAvro switchSensor -> {
                if (ConditionTypeAvro.SWITCH.equals(type)) {
                    yield switchSensor.getState();
                } else yield false;
            }

            case TemperatureSensorAvro temperatureSensor -> {
                if (ConditionTypeAvro.TEMPERATURE.equals(type)) {
                    yield conditionOperationResolve(operation, temperatureSensor.getTemperatureC(), value);
                } else yield false;
            }

            default ->
                    throw new IllegalArgumentException("Unexpected sensor type: " + sensorState.getData().getClass());
        };
    }

    private boolean conditionOperationResolve(ConditionOperationAvro operation, Integer sensorValue, Integer scenarioSetting) {
        return switch (operation) {
            case EQUALS -> sensorValue.equals(scenarioSetting);
            case LOWER_THAN -> sensorValue < scenarioSetting;
            case GREATER_THAN -> sensorValue > scenarioSetting;
            case null -> throw new IllegalStateException("Operation cannot be null");
        };
    }
}
