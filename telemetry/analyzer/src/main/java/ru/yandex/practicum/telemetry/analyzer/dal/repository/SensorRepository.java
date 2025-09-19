package ru.yandex.practicum.telemetry.analyzer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
