package org.teacher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "student_teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentTeacher {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_teacher_id")
    private Long studentTeacherId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal agreedRate; // ставка для данного ученика у этого учителя

    @Enumerated(EnumType.STRING)
    private StudentTeacherStatus status; // ACTIVE, PAUSED, ENDED

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentTeacher studentTeacher = (StudentTeacher) o;
        return Objects.equals(studentTeacherId, studentTeacher.studentTeacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentTeacherId);
    }
}
