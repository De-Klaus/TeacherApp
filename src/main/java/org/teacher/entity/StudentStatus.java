package org.teacher.entity;

import lombok.Getter;

@Getter
public enum StudentStatus {
    CREATED_BY_SYSTEM("Создан админом или учителем", "primary"),
    REGISTERED("Пользователь привязался", "warning"),
    ACTIVE("Участвует в занятиях", "success"),
    INACTIVE("Неактивен", "danger");

    private final String text;
    private final String color;

    StudentStatus(String text, String color) {
        this.text = text;
        this.color = color;
    }
}
