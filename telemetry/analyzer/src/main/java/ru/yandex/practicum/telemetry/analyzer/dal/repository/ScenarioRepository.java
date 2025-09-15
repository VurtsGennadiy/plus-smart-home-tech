package ru.yandex.practicum.telemetry.analyzer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;

import java.util.Set;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    void deleteByName(String name);

    @Query("""
            SELECT s FROM Scenario s
            JOIN s.conditions conditions
            JOIN s.actions actions
            WHERE (KEY(conditions) = ?1 OR KEY(actions) = ?1)""")
    Set<Scenario> findScenariosBySensorId(String sensorId);
}
