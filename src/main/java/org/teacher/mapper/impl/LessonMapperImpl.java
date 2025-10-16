package org.teacher.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.teacher.dto.LessonDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.Student;
import org.teacher.entity.Teacher;
import org.teacher.mapper.LessonMapper;
import org.teacher.mapper.LessonStatusMapper;

@Component
@RequiredArgsConstructor
public class LessonMapperImpl implements LessonMapper {

    private final LessonStatusMapper lessonStatusMapper;

    @Override
    public LessonDto toDto(Lesson lesson) {
        if (lesson == null) return null;
        Long studentId = lesson.getStudent() != null ? lesson.getStudent().getStudentId() : null;
        Long teacherId = lesson.getTeacher() != null ? lesson.getTeacher().getTeacherId() : null;
        return new LessonDto(lesson.getLessonId(),
                studentId,
                teacherId,
                lesson.getScheduledAt(),
                lesson.getDurationMinutes(),
                lesson.getPrice(),
                lessonStatusMapper.toDto(lesson.getStatus()),
                lesson.getHomework(),
                lesson.getFeedback()
                );
    }

    @Override
    public Lesson toEntity(LessonDto lessonDto) {
        if (lessonDto == null) return null;

        Student student = lessonDto.studentId() != null ? Student.builder().studentId(lessonDto.studentId()).build() : null;
        Teacher teacher = lessonDto.teacherId() != null ? Teacher.builder().teacherId(lessonDto.teacherId()).build() : null;

        Lesson lesson = new Lesson();
        lesson.setStudent(student);
        lesson.setTeacher(teacher);
        lesson.setScheduledAt(lessonDto.scheduledAt());
        lesson.setDurationMinutes(lessonDto.durationMinutes());
        lesson.setPrice(lessonDto.price());
        lesson.setStatus(lessonStatusMapper.toEntity(lessonDto.status()));
        lesson.setHomework(lessonDto.homework());
        lesson.setFeedback(lessonDto.feedback());
        return lesson;
    }
}
