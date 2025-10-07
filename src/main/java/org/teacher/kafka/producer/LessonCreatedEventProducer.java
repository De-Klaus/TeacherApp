package org.teacher.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.teacher.kafka.message.LessonCreatedEvent;

@Service
public class LessonCreatedEventProducer extends AbstractKafkaProducer<LessonCreatedEvent>{

    public LessonCreatedEventProducer(KafkaTemplate<String, LessonCreatedEvent> kafkaTemplate) {
        super(kafkaTemplate, "lessons.events");
    }

    @Override
    protected String extractKey(LessonCreatedEvent event) {
        return event.lessonId().toString();
    }
}
