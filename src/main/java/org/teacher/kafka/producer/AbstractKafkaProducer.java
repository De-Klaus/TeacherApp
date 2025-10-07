package org.teacher.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public abstract  class AbstractKafkaProducer<T> implements EventProducer<T> {

    protected final KafkaTemplate<String, T> kafkaTemplate;
    protected final String topic;
    protected static final Logger log = LoggerFactory.getLogger(AbstractKafkaProducer.class);

    @Override
    public void send(T event) {
        log.info("Sending event to topic {}: {}", topic, event);

        kafkaTemplate.send(topic, extractKey(event), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event {}: {}", event, ex.getMessage(), ex);
                    } else {
                        log.info("Event sent successfully: {}", event);
                    }
                });
    }

    protected abstract String extractKey(T event);
}
