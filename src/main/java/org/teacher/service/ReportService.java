package org.teacher.service;

import org.teacher.kafka.message.LessonCompletedEvent;

public interface ReportService {
    void updateLessonReport(LessonCompletedEvent event);
}
