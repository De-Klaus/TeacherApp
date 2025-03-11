package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lesson")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private LocalDate lessonDate;
    private String topic;
    private String links;
    private String assignment;
    private String homework;
    private int isActual;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacherId;

}
