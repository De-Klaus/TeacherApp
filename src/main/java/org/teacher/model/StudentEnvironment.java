package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_environment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnvironment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private String boardLink;
    private String comment;
    private LocalDate createdAt;
    private int isActual;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
}
