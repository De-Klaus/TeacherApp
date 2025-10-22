package org.teacher.entity;

import lombok.Getter;

@Getter
public enum TimeZoneOption {
    UTC_PLUS_3("UTC+3", "Москва"),
    UTC_PLUS_5("UTC+5", "Екатеринбург"),
    UTC_PLUS_7("UTC+7", "Красноярск"),
    UTC_PLUS_8("UTC+8", "Иркутск"),
    UTC_PLUS_9("UTC+9", "Якутск"),
    UTC_PLUS_10("UTC+10", "Владивосток");

    private final String value;
    private final String city;

    TimeZoneOption(String value, String city) {
        this.value = value;
        this.city = city;
    }
}
