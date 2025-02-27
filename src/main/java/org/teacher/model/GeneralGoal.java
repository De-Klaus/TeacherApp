package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "general_goal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private String goal;
    private String result;
    private String partingReason;
    private LocalDate recordDate;
    private int isActual;
}
