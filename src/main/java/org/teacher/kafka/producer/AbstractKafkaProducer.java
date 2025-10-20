package org.teacher.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public abstract  class AbstractKafkaProducer<T> implements EventProducer<T> {

    protected final KafkaTemplate<String, T> kafkaTemplate;
    protected final String topic;

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
