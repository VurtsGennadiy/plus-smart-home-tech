package collector.handler.hub;

import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события добавления сценария в хаб
 */
@Component
public class ScenarioAddedEventHandler implements HubEventHandler {
    @Override
    public void handle(HubEvent event) {
        System.out.println("ScenarioAddedEventHandler");
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED_EVENT;
    }
}
