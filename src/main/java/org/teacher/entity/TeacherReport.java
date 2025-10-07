package org.teacher.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "teacher_report")
@Getter
@Setter
public class TeacherReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_report_id")
    private Long teacherReportId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

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
        TeacherReport teacherReport = (TeacherReport) o;
        return Objects.equals(teacherReportId, teacherReport.teacherReportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherReportId);
    }

}
