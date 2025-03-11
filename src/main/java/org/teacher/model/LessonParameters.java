package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lesson_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonParameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
    private String lessonFrequency;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currencies currency;
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
    private Long durationMinutes;
    private LocalDate recordDate;
    private LocalDate deletionDate;
    private int isActual;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacherId;
}
