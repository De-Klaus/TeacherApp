package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "global_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double monthlyTotalIncome;
    private double yearlyTotalIncome;
    private double monthlyStudentIncome;
    private double yearlyStudentIncome;
    private double monthlyTotalTimeSpent;
    private double yearlyTotalTimeSpent;
    private double monthlyStudentTimeSpent;
    private double yearlyStudentTimeSpent;
    private LocalDate createdAt;
    private int isActual;
}
