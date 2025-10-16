package org.teacher.entity;

import lombok.*;

@Getter
public enum LessonStatus {
    SCHEDULED("Запланирован", "primary"),
    IN_PROGRESS("Идёт", "warning"),
    COMPLETED("Проведён", "success"),
    CANCELED("Отменён", "error");

    private final String text;
    private final String color;

    LessonStatus(String text, String color) {
        this.text = text;
        this.color = color;
    }
}
