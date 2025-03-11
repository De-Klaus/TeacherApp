package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "promotion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double correctPrice;
    private LocalDate createdAt;
    private int isActual;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacherId;

}
