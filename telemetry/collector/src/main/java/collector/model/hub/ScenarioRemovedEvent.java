package collector.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

/**
 * Событие удаления сценария из системы.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    /**
     * Название удаленного сценария. Должно содержать не менее 3 символов.
     */
    @NotNull
    @Length(min = 3)
    String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED_EVENT;
    }
}
