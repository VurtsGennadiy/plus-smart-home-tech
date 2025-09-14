package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * запускает цикл опроса событий датчиков и обработки сценариев
 */
@Slf4j
@Service
public class SnapshotProcessor implements Runnable {
    @Override
    public void run() {
        log.info("Snapshot processor run...");
    }
}
