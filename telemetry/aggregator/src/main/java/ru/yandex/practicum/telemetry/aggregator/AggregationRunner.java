package ru.yandex.practicum.telemetry.aggregator;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaConfig;
import ru.yandex.practicum.telemetry.aggregator.service.SnapshotService;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class AggregationRunner implements CommandLineRunner {
    private final SnapshotService snapshotService;
    private final KafkaConsumer<String, SensorEventAvro> kafkaConsumer;
    private final Duration poolTimeout;
    private final KafkaProducer<String, SensorsSnapshotAvro> kafkaProducer;
    private final String producerTopic;

    public AggregationRunner(KafkaConfig kafkaConfig, SnapshotService snapshotService) {
        this.snapshotService = snapshotService;

        kafkaConsumer = new KafkaConsumer<>(kafkaConfig.getConsumer().properties());
        kafkaConsumer.subscribe(kafkaConfig.getConsumer().topics().values());
        poolTimeout = kafkaConfig.getConsumer().poolTimeout();

        kafkaProducer = new KafkaProducer<>(kafkaConfig.getProducer().properties());
        producerTopic = kafkaConfig.getProducer().topics().get("sensors-snapshots");
    }

    @PreDestroy
    public void shutdown() {
        log.trace("Завершение работы, посылаю wakeup в kafka consumer");
        kafkaConsumer.wakeup();
    }

    @Override
    public void run(String... args) throws InterruptedException {
        try {
            // pool loop
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = kafkaConsumer.poll(poolTimeout);
                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    handleEvent(record.value());
                    count++;
                    if (count % 100 == 0) intermediateOffsetFixation(record); // фиксация промежуточных результатов
                }
                if (count != 0) log.trace("{} sensor events has been processed", count);
                kafkaProducer.flush();
                kafkaConsumer.commitAsync(); // фиксируем максимальный офсет обработанных записей
            }
        } catch (WakeupException ignored) {
        } catch (Exception ex) {
            log.error("Error processing sensor events", ex);
        } finally {
            log.info("Closing Kafka Consumer");
            kafkaConsumer.close(); // отключение потребителя от брокера

            log.info("Closing Kafka Producer");
            kafkaProducer.close(Duration.ofSeconds(10));
        }
    }

    private void handleEvent(SensorEventAvro event) {
        log.trace("Handle sensor event: {}", event);
        snapshotService.updateSnapshot(event).ifPresent(snapshot -> {
            ProducerRecord<String, SensorsSnapshotAvro> record = new ProducerRecord<>(producerTopic, snapshot);
            log.trace("Send in topic: {}, snapshot: {}", producerTopic, snapshot);
            kafkaProducer.send(record);
        });
    }

    private void intermediateOffsetFixation(ConsumerRecord<String, SensorEventAvro> record) {
        log.trace("Intermediate offset fixation");
        TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
        OffsetAndMetadata offset = new OffsetAndMetadata(record.offset() + 1);
        kafkaConsumer.commitAsync(Map.of(topicPartition, offset),
                (offsets, exception) -> {
                    if (exception != null) {
                        log.warn("Error performing offset fixation", exception);
                    }
                });
    }
}
