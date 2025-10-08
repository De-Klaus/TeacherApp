package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.teacher.external.whatsapp.WhatsAppClient;
import org.teacher.kafka.message.LessonCreatedEvent;
import org.teacher.service.ReminderService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final TaskScheduler scheduler;
    private final WhatsAppClient whatsAppClient;

    private static final Logger log = LoggerFactory.getLogger(ReminderServiceImpl.class);

    @Value("${lesson.student.phone}")
    private String studentPhone;

    @Value("${lesson.teacher.phone}")
    private String teacherPhone;

    @Override
    public void scheduleReminder(LessonCreatedEvent event) {
        LocalDateTime reminderTime = event.lessonTime().minusMinutes(event.reminderMinutesBefore());
        Instant startTime = reminderTime.atZone(ZoneId.systemDefault()).toInstant();
        long delay = Duration.between(Instant.now(), startTime).toMillis();

        if (delay < 0) {
            log.warn("⏰ Reminder skipped: lesson {} already started.", event.lessonId());
            return;
        }

        scheduler.schedule(() -> sendReminder(event), startTime);
        log.info("✅ Reminder scheduled for lesson {} at {}", event.lessonId(), reminderTime);
    }

    private void sendReminder(LessonCreatedEvent event) {
        log.info("📢 Sending WhatsApp reminder: Lesson {} (student={}, teacher={})",
                event.lessonId(), event.studentId(), event.teacherId());
        String message = String.format(
                "Напоминание: урок №%d для студента %d с учителем %d начинается в %s",
                event.lessonId(), event.studentId(), event.teacherId(), event.lessonTime()
        );

        try {
            // Пример: отправляем студенту
            //whatsAppClient.sendMessage(studentPhone, message);

            // Пример: отправляем учителю
            //whatsAppClient.sendMessage(teacherPhone, message);

            log.info("📢 WhatsApp reminders sent for lesson {}", event.lessonId());
        } catch (Exception e) {
            log.error("❌ Failed to send WhatsApp reminder: {}", e.getMessage(), e);
        }
    }
}