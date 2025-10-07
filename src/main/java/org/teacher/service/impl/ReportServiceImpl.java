package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.teacher.entity.Student;
import org.teacher.entity.StudentReport;
import org.teacher.entity.Teacher;
import org.teacher.entity.TeacherReport;
import org.teacher.kafka.message.LessonCompletedEvent;
import org.teacher.repository.StudentReportRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.TeacherReportRepository;
import org.teacher.service.ReportService;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final StudentReportRepository studentReportRepository;
    private final TeacherReportRepository teacherReportRepository;
    private final StudentRepository studentRepository;
    private final TeacherReportRepository teacherRepository;
    private final DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    @Override
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
        StudentReport report = studentReportRepository.findByStudent_StudentId(studentId)
                .orElseGet(() -> createNewStudentReport(studentId));

        report.setLessonsCount(report.getLessonsCount() + 1);
        report.setLessonsPrice(report.getLessonsPrice().add(price));
        report.setTotalLessonMinutes(report.getTotalLessonMinutes() + minutes);

        studentReportRepository.save(report);
    }

    private void updateTeacherReport(Long teacherId, BigDecimal price, int minutes) {
        TeacherReport report = teacherReportRepository.findByTeacher_TeacherId(teacherId)
                .orElseGet(() -> createNewTeacherReport(teacherId));

        report.setLessonsCount(report.getLessonsCount() + 1);
        report.setLessonsPrice(report.getLessonsPrice().add(price));
        report.setTotalLessonMinutes(report.getTotalLessonMinutes() + minutes);

        teacherReportRepository.save(report);
    }

    private StudentReport createNewStudentReport(Long studentId) {
        StudentReport report = new StudentReport();
        Student student = new Student();
        student.setStudentId(studentId);
        report.setStudent(student);
        report.setLessonsCount(0);
        report.setLessonsPrice(BigDecimal.ZERO);
        report.setTotalLessonMinutes(0);
        return report;
    }

    private TeacherReport createNewTeacherReport(Long teacherId) {
        TeacherReport report = new TeacherReport();
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        report.setTeacher(teacher);
        report.setLessonsCount(0);
        report.setLessonsPrice(BigDecimal.ZERO);
        report.setTotalLessonMinutes(0);
        return report;
    }
}
