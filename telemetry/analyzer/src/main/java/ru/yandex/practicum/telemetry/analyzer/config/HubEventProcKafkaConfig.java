package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@ConfigurationProperties("hub-event-proc.kafka.consumer")
@RequiredArgsConstructor
@Getter
public class HubEventProcKafkaConfig {
    private final Properties properties;
    private final String topic;
    private final Duration poolTimeout;
}
