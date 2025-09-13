package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

@ConfigurationProperties("aggregator.kafka")
@RequiredArgsConstructor
@Getter
public class KafkaConfig {
    private final ProducerConfig producer;
    private final ConsumerConfig consumer;

    public record ProducerConfig(Map<String, String> topics, Properties properties) {
    }

    public record ConsumerConfig(Map<String, String> topics, Properties properties, Duration poolTimeout) {
    }
}
