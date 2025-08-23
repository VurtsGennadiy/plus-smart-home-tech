package collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Условие активации сценария
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ScenarioCondition {
    /**
     * Идентификатор датчика, связанного с условием.
     */
    @NotBlank
    String sensorId;

    @NotNull
    ConditionType type;

    @NotNull
    Operation operation;

    @NotNull
    Integer value;

    /**
     * Типы условий, которые могут использоваться в сценариях.
     */
    enum ConditionType {
        MOTION,
        LUMINOSITY,
        SWITCH,
        TEMPERATURE,
        CO2LEVEL,
        HUMIDITY
    }

    /**
     * Операции, которые могут быть использованы в условиях.
     */
    enum Operation {
        EQUALS,
        GREATER_THAN,
        LOWER_THAN
    }
}
