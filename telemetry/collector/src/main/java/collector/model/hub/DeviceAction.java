package collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Представляет действие, которое должно быть выполнено устройством
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class DeviceAction {
    /**
     * Идентификатор датчика, связанного с действием
     */
    @NotBlank
    String sensorId;

    @NotNull
    Type type;

    /**
     * Необязательное значение, связанное с действием.
     */
    Integer value;

    /**
     * Перечисление возможных типов действий при срабатывании условия активации сценария.
     */
    enum Type {
        ACTIVATE,
        DEACTIVATE,
        INVERSE,
        SET_VALUE
    }
}
