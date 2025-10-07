package org.teacher.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.teacher.kafka.message.LessonCreatedEvent;
import org.teacher.service.ReminderService;

@Service
public class LessonCreatedEventConsumer extends AbstractKafkaConsumer<LessonCreatedEvent> {

    public LessonCreatedEventConsumer(ReminderService reminderService) {
        super(reminderService::scheduleReminder);
    }

    @KafkaListener(
            topics = "lessons.events",
            groupId = "reminder-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, LessonCreatedEvent> record, Acknowledgment ack) {
        consume(record, ack);
    }
}
