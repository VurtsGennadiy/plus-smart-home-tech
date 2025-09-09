package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.Producer;

/**
 * Обработчик события удаления сценария из хаба
 */
@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(Producer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEventProto event) {
         ScenarioRemovedEventProto scenarioRemovedEvent = event.getScenarioRemoved();

         return ScenarioRemovedEventAvro.newBuilder()
                 .setName(scenarioRemovedEvent.getName())
                 .build();
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }
}
