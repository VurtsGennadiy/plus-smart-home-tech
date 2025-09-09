package ru.yandex.practicum.telemetry.collector.kafka;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;

@ConfigurationProperties("collector.kafka.producer")
@Getter
public class KafkaProducerConfig {
    private final Properties properties;
    private final Map<String, String> topics;

    KafkaProducerConfig(Properties properties, Map<String, String> topics) {
        this.properties = properties;
        this.topics = topics;
    }
}
