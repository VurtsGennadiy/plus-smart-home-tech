package collector.handler.hub;

import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события добавления устройства в хаб
 */
@Component
public class DeviceAddedEventHandler implements HubEventHandler {
    @Override
    public void handle(HubEvent event) {
        System.out.println("DeviceAddedEventHandler");
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }
}
