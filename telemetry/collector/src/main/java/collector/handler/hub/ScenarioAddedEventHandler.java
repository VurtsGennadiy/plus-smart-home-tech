package collector.handler.hub;

import collector.kafka.Producer;
import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import collector.model.hub.ScenarioAddedEvent;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

/**
 * Обработчик события добавления сценария в хаб
 */
@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(Producer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;

        List<DeviceActionAvro> actions = scenarioAddedEvent.getActions().stream()
                .map(action ->
                        DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeAvro.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .toList();

        List<ScenarioConditionAvro> scenarioConditions = scenarioAddedEvent.getConditions().stream()
                .map(condition ->
                        ScenarioConditionAvro.newBuilder()
                                .setSensorId(condition.getSensorId())
                                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                                .setValue(condition.getValue())
                                .build())
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(actions)
                .setConditions(scenarioConditions)
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_ADDED_EVENT;
    }
}
