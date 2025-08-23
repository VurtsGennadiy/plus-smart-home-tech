package collector.handler.hub;

import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;

/**
 * Интерфейс, объединяющий все обработчики для событий HubEvent
 */
public interface HubEventHandler {
    void handle(HubEvent event);

    HubEventType getType();
}
