package ru.yandex.practicum.telemetry.analyzer.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "scenarios")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Column(name = "hub_Id", nullable = false)
    String hubId;

    @Column(name = "name", nullable = false)
    String name;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @MapKeyColumn(table = "scenario_conditions", name = "sensor_id")
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id"))
    Map<String, Condition> conditions = new HashMap<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @MapKeyColumn(table = "scenario_actions", name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    Map<String, Action> actions = new HashMap<>();
}
