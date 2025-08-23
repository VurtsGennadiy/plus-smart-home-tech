package collector.model.hub;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * Событие добавления сценария в систему
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    /**
     * Название добавленного сценария. Должно содержать не менее 3 символов.
     */
    @NotNull
    @Length(min = 3)
    String name;

    /**
     * Список условий, которые связаны со сценарием. Не может быть пустым.
     */
    @NotEmpty
    List<ScenarioCondition> conditions;

    /**
     * Список действий, которые должны быть выполнены в рамках сценария. Не может быть пустым.
     */
    @NotEmpty
    List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED_EVENT;
    }
}
