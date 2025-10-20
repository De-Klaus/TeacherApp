package org.teacher.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
public abstract class AbstractKafkaConsumer<T> {

    private final EventConsumer<T> eventConsumer;

    protected AbstractKafkaConsumer(EventConsumer<T> eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    public void consume(ConsumerRecord<String, T> record, Acknowledgment ack) {
        T event = record.value();

        if (event == null) {
            log.warn("⚠️ Received null event. Skipping message at offset={}", record.offset());
            ack.acknowledge();
            return;
        }

        log.info("📩 [{}] Received from topic={} partition={} offset={}: {}",
                getClass().getSimpleName(), record.topic(), record.partition(), record.offset(), event);

        try {
            eventConsumer.eventConsumer(event);
            log.info("✅ Successfully handled event {}", event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("❌ Failed to handle event due to {}", e.getMessage(), e);
            // не подтверждаем ack — Kafka выполнит retry / DLQ
        }
    }
}
