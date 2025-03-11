package org.teacher.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String middleName;

    private String city;

    private LocalDate birthDate;

    @NotBlank(message = "Time zone is required")
    private String timeZone;

    @NotBlank(message = "Platform is required")
    private String platform;

    @NotNull(message = "Creation date is required")
    private LocalDate createdAt;

    @Min(value = 1900, message = "School start year must be valid")
    private int schoolStartYear;

    @Min(value = 1, message = "Current grade must be at least 1")
    @Max(value = 12, message = "Current grade must be at most 12")
    private int currentGrade;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacherId;
}
