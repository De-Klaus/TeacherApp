package org.teacher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long teacherId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    @NotBlank(message = "Subject is required")
    @Column(nullable = false, length = 100)
    private String subject;

    @NotBlank(message = "Time zone is required")
    private String timeZone;


    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<StudentTeacher> students = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(teacherId, teacher.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId);
    }
}
