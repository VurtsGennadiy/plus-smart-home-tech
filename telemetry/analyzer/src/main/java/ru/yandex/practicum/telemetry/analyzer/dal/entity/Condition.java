package ru.yandex.practicum.telemetry.analyzer.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Entity
@Table(name = "conditions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    ConditionTypeAvro type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    ConditionOperationAvro operation;

    @Column(name = "value")
    Integer value;

    public static Condition ofScenarioConditionAvro(ScenarioConditionAvro avro) {
        Integer value = switch (avro.getValue()) {
            case Integer intValue -> intValue;
            case Boolean boolValue -> boolValue ? 1 : 0;
            case null -> null;
            default -> throw new IllegalStateException(
                    "field 'value' of ScenarioConditionAvro must be one of next types: Integer, Boolean, null");
        };

        return Condition.builder()
                .type(avro.getType())
                .operation(avro.getOperation())
                .value(value)
                .build();
    }
}
