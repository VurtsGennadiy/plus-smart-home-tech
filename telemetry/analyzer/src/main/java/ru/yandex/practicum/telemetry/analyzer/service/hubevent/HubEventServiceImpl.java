package ru.yandex.practicum.telemetry.analyzer.service.hubevent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.entity.HubEventType;
import ru.yandex.practicum.telemetry.analyzer.service.hubevent.handler.HubEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * обработка событий хаба:
 * DEVICE_ADDED,
 * DEVICE_REMOVED,
 * SCENARIO_ADDED,
 * SCENARIO_REMOVED
 */
@Service
@Slf4j
public class HubEventServiceImpl implements HubEventService {
    private final Map<HubEventType, HubEventHandler> handlers; // сопоставление типа события с обработчиком

    public HubEventServiceImpl(List<HubEventHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getHubEventType, Function.identity()));
    }

    @Override
    public void handle(HubEventAvro event) {
        log.trace("Handle hub event: {}", event);
        HubEventType eventType = HubEventType.ofHubEventAvro(event);
        try {
            handlers.get(eventType).handle(event);
        } catch (Exception ex) {
            log.error("Error handle hub event", ex);
        }
    }
}
