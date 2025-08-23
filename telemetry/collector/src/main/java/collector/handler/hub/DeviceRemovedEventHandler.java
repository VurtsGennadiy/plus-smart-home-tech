package collector.handler.hub;

import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import org.springframework.stereotype.Component;

/**
 * Обработчик события удаления устройства из хаба
 */
@Component
public class DeviceRemovedEventHandler implements HubEventHandler {
    @Override
    public void handle(HubEvent event) {
        System.out.println("DeviceRemovedEventHandler");
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED_EVENT;
    }
}
