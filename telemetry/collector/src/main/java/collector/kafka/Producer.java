package collector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class Producer implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final TopicResolver topicResolver;

    public Producer(KafkaProducerConfig config, TopicResolver topicResolver) {
        producer = new KafkaProducer<>(config.getProperties());
        this.topicResolver = topicResolver;
    }

    public void send(SpecificRecordBase message, TopicType topicType) {
        String topic = topicResolver.getTopicName(topicType);
        log.info("Отправляю в топик {} сообщение: {}", topic, message);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, message);
        producer.send(record);
    }

    @Override
    public void close() {
        producer.close(Duration.ofSeconds(10));
    }
}
