package ru.yandex.practicum.telemetry.analyzer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.analyzer.service.hubevent.HubEventProcessor;
import ru.yandex.practicum.telemetry.analyzer.service.snapshot.SnapshotProcessor;

@RequiredArgsConstructor
@Service
public class AnalyzeRunner implements CommandLineRunner {
    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;

    @Override
    public void run(String... args) {
        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventProc");
        hubEventsThread.start();

        snapshotProcessor.run();
    }
}
