package ru.yandex.practicum.telemetry.analyzer;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.service.hubevent.HubEventServiceImpl;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
/*@SpringJUnitConfig(classes = {
        HubEventServiceImpl.class,
        DeviceAddedHubEventHandler.class,
        org.slf4j.LoggerFactory.class
})*/

@SpringBootTest
public class HubEventHandlerServiceTest {
    private final HubEventServiceImpl service;

    HubEventAvro addDeviceEvent = HubEventAvro.newBuilder()
            .setHubId("test_hub")
            .setTimestamp(Instant.now())
            .setPayload(DeviceAddedEventAvro.newBuilder()
                    .setId("humidity sensor")
                    .setType(DeviceTypeAvro.CLIMATE_SENSOR)
                    .build())
            .build();

    HubEventAvro removeDeviceEvent = HubEventAvro.newBuilder()
            .setHubId("test_hub")
            .setTimestamp(Instant.now())
            .setPayload(DeviceRemovedEventAvro.newBuilder()
                    .setId("увлажнитель воздуха")
                    .build())
            .build();

    HubEventAvro addScenarioHubEvent = HubEventAvro.newBuilder()
            .setHubId("test_hub")
            .setTimestamp(Instant.now())
            .setPayload(ScenarioAddedEventAvro.newBuilder()
                    .setName("scenario_name")
                    .setConditions(List.of(ScenarioConditionAvro.newBuilder()
                            .setSensorId("humidity sensor")
                            .setType(ConditionTypeAvro.HUMIDITY)
                            .setOperation(ConditionOperationAvro.LOWER_THAN)
                            .setValue(80)
                            .build()))
                    .setActions(List.of(DeviceActionAvro.newBuilder()
                            .setSensorId("увлажнитель воздуха")
                            .setType(ActionTypeAvro.ACTIVATE)
                            .setValue(null)
                            .build()))
                    .build())
            .build();

    HubEventAvro removeScenarioHubEvent = HubEventAvro.newBuilder()
            .setHubId("test_hub")
            .setTimestamp(Instant.now())
            .setPayload(ScenarioRemovedEventAvro.newBuilder()
                    .setName("scenario_name")
                    .build())
            .build();

    @Test
    void handleDeviceAddedEvent() {
        service.handle(addDeviceEvent);
    }

    @Test
    void handleDeviceRemovedEvent() {
        service.handle(removeDeviceEvent);
    }

    @Test
    void handleScenarioAddEvent() {
        service.handle(addScenarioHubEvent);
    }

    @Test
    void handleScenarioRemovedEvent() {
        service.handle(removeScenarioHubEvent);
    }
}
