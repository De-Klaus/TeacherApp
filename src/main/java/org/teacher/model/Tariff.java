package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tariff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tariffName;
    private Double price;
    private Long durationMinutes;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currencies currency;
    private String comment;
    private LocalDate createdAt;
    private int isActual;
}
