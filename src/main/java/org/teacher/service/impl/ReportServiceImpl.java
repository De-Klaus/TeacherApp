package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.entity.Student;
import org.teacher.entity.StudentReport;
import org.teacher.entity.Teacher;
import org.teacher.entity.TeacherReport;
import org.teacher.kafka.message.LessonCompletedEvent;
import org.teacher.repository.StudentReportRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.TeacherReportRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.service.ReportService;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final StudentReportRepository studentReportRepository;
    private final TeacherReportRepository teacherReportRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    @Override
    @Transactional
    public void updateLessonReport(LessonCompletedEvent event) {
        //TODO
        // Логика для отчета:
        // например, обновляем посещаемость ученика и учителя

//        if(studentRepository.existsById(event.studentId())&&teacherRepository.existsById(event.teacherId())) {
            int duration = event.durationMinutes() != null ? event.durationMinutes() : 0;
            updateStudentReport(event.studentId(), event.price(), duration);
            updateTeacherReport(event.teacherId(), event.price(), duration);
            log.info("📊 Report updated: studentId={}, teacherId={}", event.studentId(), event.teacherId());
        /*} else {
            log.warn("Skipping report update: studentId={} or teacherId={} not found",
                    event.studentId(), event.teacherId());
        }*/

    }

    private void updateStudentReport(Long studentId, BigDecimal price, int minutes) {
        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));

        StudentReport report = studentReportRepository.findByStudent_StudentId(studentId)
                .orElseGet(() -> createNewStudentReport(student));

        report.setLessonsCount(report.getLessonsCount() + 1);
        report.setLessonsPrice(report.getLessonsPrice().add(price));
        report.setTotalLessonMinutes(report.getTotalLessonMinutes() + minutes);

        studentReportRepository.save(report);
    }

    private void updateTeacherReport(Long teacherId, BigDecimal price, int minutes) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + teacherId));

        TeacherReport report = teacherReportRepository.findByTeacher_TeacherId(teacherId)
                .orElseGet(() -> createNewTeacherReport(teacher));

        report.setLessonsCount(report.getLessonsCount() + 1);
        report.setLessonsPrice(report.getLessonsPrice().add(price));
        report.setTotalLessonMinutes(report.getTotalLessonMinutes() + minutes);

        teacherReportRepository.save(report);
    }

    private StudentReport createNewStudentReport(Student student) {
        return StudentReport.builder()
                .student(student)
                .lessonsCount(0)
                .lessonsPrice(BigDecimal.ZERO)
                .totalLessonMinutes(0)
                .build();
    }

    private TeacherReport createNewTeacherReport(Teacher teacher) {
        return TeacherReport.builder()
                .teacher(teacher)
                .lessonsCount(0)
                .lessonsPrice(BigDecimal.ZERO)
                .totalLessonMinutes(0)
                .build();
    }
}
