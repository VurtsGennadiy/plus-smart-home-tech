package ru.yandex.practicum.telemetry.analyzer.service.hubevent;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.config.HubEventProcKafkaConfig;

import java.time.Duration;
import java.util.List;

/**
 * запускает цикл опроса и обработки событий хабов
 */
@Service
@Slf4j
public class HubEventProcessor implements Runnable {
    private final HubEventService hubEventService;
    private final KafkaConsumer<String, HubEventAvro> kafkaConsumer;
    private final Duration poolTimeout;

    public HubEventProcessor(HubEventProcKafkaConfig kafkaConfig, HubEventService hubEventService) {
        kafkaConsumer = new KafkaConsumer<>(kafkaConfig.getProperties());
        kafkaConsumer.subscribe(List.of(kafkaConfig.getTopic()));
        poolTimeout = kafkaConfig.getPoolTimeout();
        this.hubEventService = hubEventService;
    }

    @PreDestroy
    public void shutdown() {
        log.trace("Завершение работы HubEventProcessor, посылаю wakeup в kafka consumer");
        kafkaConsumer.wakeup();
    }

    @Override
    public void run() {
        // pool loop
        try {
            log.info("HubEvent Processor runed");
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = kafkaConsumer.poll(poolTimeout);
                records.forEach(record -> hubEventService.handle(record.value()));
                kafkaConsumer.commitAsync();
            }
        } catch (WakeupException ignore) {
        } catch (Exception ex) {
            log.error("Error processing hub event", ex);
        } finally {
            log.info("Closing Kafka Consumer");
            kafkaConsumer.close(); // отключение потребителя от брокера
        }
    }
}
