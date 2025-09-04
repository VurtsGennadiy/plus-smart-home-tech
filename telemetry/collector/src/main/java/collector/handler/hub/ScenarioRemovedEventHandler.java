package collector.handler.hub;

import collector.kafka.Producer;
import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import collector.model.hub.ScenarioRemovedEvent;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

/**
 * Обработчик события удаления сценария из хаба
 */
@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(Producer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
         ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;

         return ScenarioRemovedEventAvro.newBuilder()
                 .setName(scenarioRemovedEvent.getName())
                 .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_REMOVED_EVENT;
    }
}
