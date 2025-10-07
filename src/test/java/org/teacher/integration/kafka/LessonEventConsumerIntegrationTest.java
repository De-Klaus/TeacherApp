package org.teacher.integration.kafka;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.teacher.kafka.message.LessonCreatedEvent;
import org.teacher.kafka.producer.LessonCreatedEventProducer;
import org.teacher.service.ReminderService;

import java.time.LocalDateTime;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "lessons.events" })
class LessonEventConsumerIntegrationTest extends AbstractKafkaIntegrationTest {

    @Autowired
    private LessonCreatedEventProducer lessonEventProducer;

    @SpyBean
    private ReminderService reminderService; // <- важно Spy на интерфейс, а не на имплементацию

    @Test
    void testLessonCreatedEventTriggersReminder() {
        LessonCreatedEvent event = new LessonCreatedEvent(
                1L, 10L, 20L, LocalDateTime.now().plusMinutes(2), 1
        );

        lessonEventProducer.send(event);

        waitUntil(() -> {
            Mockito.verify(reminderService, Mockito.times(1)).scheduleReminder(event);
                });
    }
}

