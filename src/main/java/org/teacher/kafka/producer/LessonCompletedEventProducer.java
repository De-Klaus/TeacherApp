package org.teacher.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.teacher.kafka.message.LessonCompletedEvent;

@Service
public class LessonCompletedEventProducer extends AbstractKafkaProducer<LessonCompletedEvent> {

    public LessonCompletedEventProducer(KafkaTemplate<String, LessonCompletedEvent> kafkaTemplate) {
        super(kafkaTemplate, "lessons.completed.events");
    }

    @Override
    protected String extractKey(LessonCompletedEvent event) {
        return event.lessonId().toString();
    }
}
