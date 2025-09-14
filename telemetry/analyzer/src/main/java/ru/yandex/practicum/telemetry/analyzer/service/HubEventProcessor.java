package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * запускает цикл опроса и обработки событий хабов
 */
@Service
@Slf4j
public class HubEventProcessor implements Runnable {
    @Override
    public void run() {
        log.info("HubEvent Processor run...");
    }
}
