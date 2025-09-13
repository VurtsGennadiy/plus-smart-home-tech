package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SnapshotServiceImpl implements SnapshotService {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>(); // состояние датчиков для каждого хаба, ключ - hubId

    @Override
    public Optional<SensorsSnapshotAvro> updateSnapshot(SensorEventAvro event) {
        String hubId = event.getHubId();
        String eventId = event.getId();

        SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        // создаём снапшот для хаба, если снапшот не существует
        if (!snapshots.containsKey(hubId)) {
            SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>(Map.of(eventId, newSensorState)))
                    .build();

            snapshots.put(hubId, snapshot);
            log.trace("Snapshot hubId: {} created", hubId);
            return Optional.of(snapshot);
        } else {
            SensorsSnapshotAvro snapshot = snapshots.get(hubId);
            SensorStateAvro oldSensorState = snapshot.getSensorsState().get(eventId);
            log.trace("Old snapshot: {}", snapshot);

            // обновляем снапшот если в нем ещё нет события для датчика
            // или данные с датчика изменились (timestamp события датчика в тестах может совпадать)
            if (oldSensorState == null ||
                    (!newSensorState.getTimestamp().isBefore(oldSensorState.getTimestamp())
                            && !oldSensorState.getData().equals(newSensorState.getData()))) {
                snapshot.setTimestamp(newSensorState.getTimestamp());
                snapshot.getSensorsState().put(eventId, newSensorState);
                log.trace("Snapshot hubId: {} are updated", hubId);
                return Optional.of(snapshot);
            } else {
                log.trace("Snapshot hubId: {} are not updated", hubId);
                return Optional.empty();
            }
        }
    }
}
