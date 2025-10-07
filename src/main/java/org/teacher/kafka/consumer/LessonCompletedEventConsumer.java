package org.teacher.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.teacher.kafka.message.LessonCompletedEvent;
import org.teacher.service.ReportService;


@Service
public class LessonCompletedEventConsumer extends AbstractKafkaConsumer<LessonCompletedEvent> {

    public LessonCompletedEventConsumer(ReportService reportService) {
        super(reportService::updateLessonReport);
    }

    @KafkaListener(
            topics = "lessons.completed.events",
            groupId = "report-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, LessonCompletedEvent> record, Acknowledgment ack) {
        consume(record, ack);
    }
}
