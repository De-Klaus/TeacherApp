package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.LessonDto;
import org.teacher.dto.StudentDto;
import org.teacher.dto.TeacherDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.kafka.message.LessonCompletedEvent;
import org.teacher.kafka.producer.LessonCompletedEventProducer;
import org.teacher.mapper.LessonMapper;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.service.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StudentTeacherRepository studentTeacherRepository;
    private final LessonRepository lessonRepository;
    private final StudentTeacherService studentTeacherService;
    private final LessonMapper lessonMapper;
    private final LessonCompletedEventProducer lessonEventProducer;
    private final AuthService authService;

    @Override
    @Transactional
    public LessonDto addLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);

        if(lesson.getTeacher()==null){
            studentTeacherService.findActiveByStudent(lesson.getStudent().getStudentId())
                    .ifPresent(st -> {
                        lesson.setTeacher(st.getTeacher());
                    });
        }

        if (lesson.getPrice() == null) {
            studentTeacherService.findActiveByStudentAndTeacher(
                    lesson.getStudent().getStudentId(), lesson.getTeacher().getTeacherId())
                    .ifPresent(st -> lesson.setPrice(st.getAgreedRate()));
        }
        Lesson saved = lessonRepository.save(lesson);

        lessonEventProducer.send(new LessonCompletedEvent(
                saved.getLessonId(),
                saved.getStudent().getStudentId(),
                saved.getTeacher().getTeacherId(),
                saved.getPrice(),
                saved.getDurationMinutes()
        ));

        return lessonMapper.toDto(saved);
    }

    @Override
    public Optional<LessonDto> getById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .map(lessonMapper::toDto);
    }

    @Override
    public List<LessonDto> getAll() {
        UserResponseDto currentUser = authService.getCurrentUser();
        if (currentUser.hasRole(Role.TEACHER)) {
            TeacherDto teacher = teacherService.findByUserId(currentUser.userId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher profile not found"));
            return lessonRepository.findByTeacher_TeacherId(teacher.teacherId())
                    .stream()
                    .map(lessonMapper::toDto)
                    .toList();
        } else if (currentUser.hasRole(Role.STUDENT)) {
            StudentDto student = studentService.findByUserId(currentUser.userId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher profile not found"));
            return lessonRepository.findByStudent_StudentId(student.studentId())
                    .stream()
                    .map(lessonMapper::toDto)
                    .toList();
        } else if (currentUser.hasRole(Role.ADMIN)) {
            return lessonRepository.findAll()
                    .stream()
                    .map(lessonMapper::toDto)
                    .toList();
        }
        return Collections.emptyList();
    }

    @Override
    public LessonDto update(Long lessonId, LessonDto dto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + lessonId));

        lesson.setScheduledAt(dto.scheduledAt());
        lesson.setDurationMinutes(dto.durationMinutes());
        lesson.setPrice(dto.price());
        lesson.setStatus(dto.status());
        lesson.setHomework(dto.homework());
        lesson.setFeedback(dto.feedback());

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toDto(saved);
    }

    @Override
    public void delete(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
