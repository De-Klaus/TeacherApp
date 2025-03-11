package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "education_expense")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private double cost;
    private String comment;
    private LocalDate purchaseDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacherId;
}
