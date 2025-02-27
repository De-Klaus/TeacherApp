package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_expense")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private String item;
    private double cost;
    private String comment;
    private LocalDate purchaseDate;
}
