package ru.yandex.practicum.telemetry.analyzer.service.snapshot;

import com.google.protobuf.Timestamp;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.SnapshotProcKafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Action;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.mapper.AvroProtoMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * запускает цикл опроса и обработки снапшотов
 */
@Slf4j
@Service
public class SnapshotProcessor implements Runnable {
    private final KafkaConsumer<String, SensorsSnapshotAvro> kafkaConsumer;
    private final Duration poolTimeout;
    private final SnapshotService snapshotService;

    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotProcessor(SnapshotProcKafkaConfig kafkaConfig,
                             SnapshotService snapshotService) {
        kafkaConsumer = new KafkaConsumer<>(kafkaConfig.getProperties());
        kafkaConsumer.subscribe(List.of(kafkaConfig.getTopic()));
        poolTimeout = kafkaConfig.getPoolTimeout();
        this.snapshotService = snapshotService;
    }

    @PreDestroy
    public void shutdown() {
        log.trace("Завершение работы SnapshotProcessor, посылаю wakeup в kafka consumer");
        kafkaConsumer.wakeup();
    }

    @Override
    public void run() {
        // pool loop
        try {
            log.info("Snapshot Processor runed");
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = kafkaConsumer.poll(poolTimeout);
                records.forEach(record -> {
                    List<Scenario> scenarios = snapshotService.findMatchingScenarios(record.value());
                    List<DeviceActionRequest> requests = mapScenariosToDeviceActionRequest(scenarios, record.value());
                    sendDeviceActionRequests(requests);
                });
                kafkaConsumer.commitAsync();
            }
        } catch (WakeupException ignore) {
        } catch (Exception ex) {
            log.error("Error processing snapshot", ex);
        } finally {
            log.info("Closing Kafka Consumer");
            kafkaConsumer.close(); // отключение потребителя от брокера
        }
    }

    /**
     * Преобразование сценариев в формат запроса на исполнение для последующей отправки в HubRouterController
     */
    private List<DeviceActionRequest> mapScenariosToDeviceActionRequest(List<Scenario> scenarios, SensorsSnapshotAvro snapshot) {
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(snapshot.getTimestamp().getEpochSecond())
                .setNanos(snapshot.getTimestamp().getNano())
                .build();

        List<DeviceActionRequest> requests = new ArrayList<>();
        for (Scenario scenario : scenarios) {
            for (Map.Entry<String, Action> action : scenario.getActions().entrySet()) {
                DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                        .setSensorId(action.getKey())
                        .setType(AvroProtoMapper.toActionTypeProto(action.getValue().getType()))
                        .setValue(action.getValue().getValue())
                        .build();

                DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                        .setHubId(scenario.getHubId())
                        .setScenarioName(scenario.getName())
                        .setTimestamp(timestamp)
                        .setAction(deviceActionProto)
                        .build();

                requests.add(deviceActionRequest);
            }
        }
        return requests;
    }

    /**
     * Отправка запросов на исполнение в HubRouterController
     */
    private void sendDeviceActionRequests(List<DeviceActionRequest> requests) {
        log.trace("Sending {} DeviceActionRequest", requests.size());
        for (DeviceActionRequest request : requests) {
            try {
                log.trace("Sending DeviceActionRequest {}", request);
                hubRouterClient.handleDeviceAction(request);
            } catch (Exception e) {
                log.error("Error sending DeviceActionRequest {}", request, e);
            }
        }
    }
}
