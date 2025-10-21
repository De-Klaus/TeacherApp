package org.teacher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status; // например: CREATED_BY_TEACHER, REGISTERED, ACTIVE

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private LocalDate birthDate;
    private String phoneNumber;

    private String city;
    @NotBlank(message = "Time zone is required")
    private String timeZone;


    // Учебная часть
    @Min(value = 1, message = "Current grade must be at least 1")
    @Max(value = 12, message = "Current grade must be at most 12")
    private Integer grade;
    private String school;

    // Опционально: связь с занятиями
    @Builder.Default
    @OneToMany(mappedBy = "student")
    private List<Lesson> lessons = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private List<StudentTeacher> teachers = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
