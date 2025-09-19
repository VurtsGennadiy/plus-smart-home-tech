package ru.yandex.practicum.telemetry.analyzer.service.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;

import java.util.List;

public interface SnapshotService {
    List<Scenario> findMatchingScenarios(SensorsSnapshotAvro snapshot);
}
