package collector.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * Событие, сигнализирующее о добавлении нового устройства в систему.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    /**
     * Идентификатор добавленного устройства.
     */
    @NotNull
    String id;

    @NotNull
    DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }
}
