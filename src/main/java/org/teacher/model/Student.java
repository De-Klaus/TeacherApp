package org.teacher.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String city;
    private LocalDate birthDate;
    private String timeZone;
    private String platform;
    private LocalDate createdAt;
    private int schoolStartYear;
    private int currentGrade;
}
