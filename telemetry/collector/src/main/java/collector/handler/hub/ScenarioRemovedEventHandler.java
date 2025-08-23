package collector.handler.hub;

import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события удаления сценария из хаба
 */
@Component
public class ScenarioRemovedEventHandler implements HubEventHandler {
    @Override
    public void handle(HubEvent event) {
        System.out.println("ScenarioRemovedEventHandler");
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED_EVENT;
    }
}
