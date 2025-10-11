package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.teacher.dto.LessonDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.kafka.message.LessonCompletedEvent;
import org.teacher.kafka.producer.LessonCompletedEventProducer;
import org.teacher.mapper.LessonMapper;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final StudentTeacherService studentTeacherService;
    private final LessonMapper lessonMapper;
    private final LessonCompletedEventProducer lessonEventProducer;
    private final AuthService authService;

    @Override
    @Transactional
    public LessonDto addLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        UserResponseDto currentUser = authService.getCurrentUser();

        if (currentUser.hasRole(Role.TEACHER)) {
            Teacher teacher = teacherRepository.findByUser_UserId(currentUser.userId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found for current user"));
            lesson.setTeacher(teacher);
        }

        if (lesson.getStudent() == null || lesson.getStudent().getStudentId() == null) {
            throw new IllegalArgumentException("Student must be specified when creating a lesson");
        }

        if(lesson.getTeacher()==null){
            studentTeacherService.findActiveByStudent(lesson.getStudent().getStudentId())
                    .ifPresentOrElse(
                            st -> lesson.setTeacher(st.getTeacher()),
                            () -> { throw new EntityNotFoundException("No active teacher for this student"); }
                    );
        }

        if (lesson.getPrice() == null) {
            studentTeacherService.findActiveByStudentAndTeacher(
                    lesson.getStudent().getStudentId(), lesson.getTeacher().getTeacherId())
                    .ifPresentOrElse(
                            st -> lesson.setPrice(st.getAgreedRate()),
                            () -> { throw new IllegalStateException("No active rate between student and teacher"); }
                    );
        }
        if (lesson.getStatus() == null) {
            lesson.setStatus(LessonStatus.SCHEDULED);
        }

        if (lesson.getScheduledAt() == null) {
            throw new IllegalArgumentException("Scheduled date/time is required");
        }

        Lesson saved = lessonRepository.save(lesson);

        // --- Отложенная отправка события (после commit) ---
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                lessonEventProducer.send(new LessonCompletedEvent(
                        saved.getLessonId(),
                        saved.getStudent().getStudentId(),
                        saved.getTeacher().getTeacherId(),
                        saved.getPrice(),
                        saved.getDurationMinutes()
                ));
            }
        });

        /*log.info("Lesson created: lessonId={}, teacherId={}, studentId={}",
                saved.getLessonId(), saved.getTeacher().getTeacherId(), saved.getStudent().getStudentId());*/

        return lessonMapper.toDto(saved);
    }

    @Override
    public Optional<LessonDto> getById(Long lessonId) {
        UserResponseDto currentUser = authService.getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        if (canAccessLesson(currentUser, lesson)) {
            return Optional.of(lessonMapper.toDto(lesson));
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public List<LessonDto> getAll() {
        UserResponseDto currentUser = authService.getCurrentUser();
        if (currentUser.hasRole(Role.TEACHER)) {
            return lessonRepository.findAllByTeacher_User_UserId(currentUser.userId())
                    .stream()
                    .map(lessonMapper::toDto)
                    .toList();
        } else if (currentUser.hasRole(Role.STUDENT)) {
            return lessonRepository.findAllByStudent_User_UserId(currentUser.userId())
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
        UserResponseDto currentUser = authService.getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + lessonId));

        lesson.setScheduledAt(dto.scheduledAt());
        lesson.setDurationMinutes(dto.durationMinutes());
        lesson.setPrice(dto.price());
        lesson.setStatus(dto.status());
        lesson.setHomework(dto.homework());
        lesson.setFeedback(dto.feedback());

        if (canAccessLesson(currentUser, lesson)) {
            Lesson saved = lessonRepository.save(lesson);
            return lessonMapper.toDto(saved);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public void delete(Long lessonId) {
        UserResponseDto currentUser = authService.getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        if (canAccessLesson(currentUser, lesson)) {
            lessonRepository.deleteById(lessonId);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    private boolean canAccessLesson(UserResponseDto user, Lesson lesson) {
        return user.hasRole(Role.ADMIN)
                || (user.hasRole(Role.TEACHER)
                && lesson.getTeacher().getUser().getUserId().equals(user.userId()))
                || (user.hasRole(Role.STUDENT)
                && lesson.getStudent().getUser().getUserId().equals(user.userId()));
    }
}
