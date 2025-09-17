package org.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long studentId;
    private Long teacherId;
    private LocalDateTime lessonDate;
    private String topic;
    private String links;
    private String assignment;
    private String homework;
    private int isActual;
}
