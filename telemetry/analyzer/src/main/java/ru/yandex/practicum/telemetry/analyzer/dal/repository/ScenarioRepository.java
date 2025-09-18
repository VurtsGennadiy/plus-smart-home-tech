package ru.yandex.practicum.telemetry.analyzer.dal.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    @EntityGraph(attributePaths = {"conditions", "actions"})
    List<Scenario> findScenariosByHubId(String hubId);

    void deleteByName(String name);
}
