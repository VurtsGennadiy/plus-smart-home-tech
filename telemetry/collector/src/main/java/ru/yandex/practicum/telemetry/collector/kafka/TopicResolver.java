package ru.yandex.practicum.telemetry.collector.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;

/**
 * Класс сопоставляет значение enum TopicType со строковым названием топика из конфигурации
 */
@Component
@RequiredArgsConstructor
public class TopicResolver {
    private final KafkaProducerConfig kafkaConfig;
    private final EnumMap<TopicType, String> topicsMap = new EnumMap<>(TopicType.class);

    @PostConstruct
    private void init() {
        topicsMap.put(TopicType.SENSORS_EVENTS_TOPIC, kafkaConfig.getTopics().get("sensors-events"));
        topicsMap.put(TopicType.HUBS_EVENTS_TOPIC, kafkaConfig.getTopics().get("hubs-events"));

        // проверка, что все enum-значения сконфигурированы
        for (TopicType type : TopicType.values()) {
            if (!topicsMap.containsKey(type)) {
                throw new IllegalStateException(String.format("Для TopicType %s не сконфигурирован топик", type.name()));
            }
        }
    }

    public String getTopicName(TopicType topicType) {
        return topicsMap.get(topicType);
    }
}
