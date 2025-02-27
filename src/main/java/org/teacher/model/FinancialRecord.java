package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "financial_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private double lessonIncome;
    private double otherIncome;
    private double expenses;
    private double profit;
}
