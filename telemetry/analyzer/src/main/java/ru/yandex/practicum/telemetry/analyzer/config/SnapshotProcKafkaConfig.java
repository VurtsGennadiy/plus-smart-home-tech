package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@ConfigurationProperties("snapshot-proc.kafka.consumer")
@RequiredArgsConstructor
@Getter
public class SnapshotProcKafkaConfig {
    private final Properties properties;
    private final String topic;
    private final Duration poolTimeout;
}
