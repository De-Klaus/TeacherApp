package org.teacher.service;

import org.teacher.kafka.message.LessonCreatedEvent;

public interface ReminderService {
    void scheduleReminder(LessonCreatedEvent event);
}
