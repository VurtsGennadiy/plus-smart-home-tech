package collector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Producer implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;
    public final String SENSORS_EVENTS_TOPIC;
    public final String HUBS_EVENTS_TOPIC;

    public Producer(KafkaProducerConfig config) {
        producer = new KafkaProducer<>(config.getProperties());
        SENSORS_EVENTS_TOPIC = config.getTopics().get("sensors-events");
        HUBS_EVENTS_TOPIC = config.getTopics().get("hubs-events");
    }

    public void send(SpecificRecordBase message, String topic) {
        log.info("Отправляю в топик {} сообщение: {}", topic, message);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, message);
        producer.send(record);
    }

    @Override
    public void close() throws Exception {
        producer.flush();
        producer.close();
    }
}
