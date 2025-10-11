package org.teacher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "student_report")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_report_id")
    private Long studentReportId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "lessons_count", nullable = false)
    private Integer lessonsCount = 0;

    @Column(name = "lessons_price", nullable = false)
    private BigDecimal lessonsPrice  = new BigDecimal(0);

    @Column(name = "total_lesson_minutes", nullable = false)
    private Integer totalLessonMinutes = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentReport studentReport = (StudentReport) o;
        return Objects.equals(studentReportId, studentReport.studentReportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentReportId);
    }
}
