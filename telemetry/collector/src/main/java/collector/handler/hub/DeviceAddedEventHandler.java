package collector.handler.hub;

import collector.kafka.Producer;
import collector.model.hub.DeviceAddedEvent;
import collector.model.hub.HubEvent;
import collector.model.hub.HubEventType;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

/**
 * Обработчик события добавления устройства в хаб
 */
@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(Producer producer) {
        super(producer);
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;

        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                .build();
    }
}
