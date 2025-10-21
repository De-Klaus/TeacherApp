package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
import org.teacher.mapper.LessonStatusMapper;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.service.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final StudentTeacherService studentTeacherService;
    private final LessonMapper lessonMapper;
    private final LessonStatusMapper lessonStatusMapper;
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
    @Transactional(readOnly = true)
    public Page<LessonDto> getAll(Pageable pageable) {
        UserResponseDto currentUser = authService.getCurrentUser();
        if (currentUser.hasRole(Role.TEACHER)) {
            return lessonRepository.findAllByTeacher_User_UserId(currentUser.userId(), pageable)
                    .map(lessonMapper::toDto);
        } else if (currentUser.hasRole(Role.STUDENT)) {
            return lessonRepository.findAllByStudent_User_UserId(currentUser.userId(), pageable)
                    .map(lessonMapper::toDto);
        } else if (currentUser.hasRole(Role.ADMIN)) {
            return lessonRepository.findAll(pageable)
                    .map(lessonMapper::toDto);
        }
        return Page.empty();
    }

    @Override
    public LessonDto update(Long lessonId, LessonDto dto) {
        UserResponseDto currentUser = authService.getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + lessonId));

        // Проверка прав до изменения данных
        if (!canAccessLesson(currentUser, lesson)) {
            throw new AccessDeniedException("Access denied");
        }

        if (dto.scheduledAt() != null) {
            lesson.setScheduledAt(dto.scheduledAt());
        }
        if (dto.durationMinutes() != null) {
            lesson.setDurationMinutes(dto.durationMinutes());
        }
        if (dto.price() != null) {
            lesson.setPrice(dto.price());
        }
        if (dto.status() != null) {
            lesson.setStatus(lessonStatusMapper.toEntity(dto.status()));
        }
        if (dto.homework() != null) {
            lesson.setHomework(dto.homework());
        }
        if (dto.feedback() != null) {
            lesson.setFeedback(dto.feedback());
        }
        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toDto(updated);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public LessonDto updateLessonStatus(Long lessonId, LessonStatus status) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + lessonId));

        // Можно добавить проверку перехода статусов (best practice)
        if (!isValidStatusTransition(lesson.getStatus(), status)) {
            throw new IllegalStateException("Invalid status transition from " + lesson.getStatus() + " to " + status);
        }

        lesson.setStatus(status);

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toDto(saved);
    }

    private boolean isValidStatusTransition(LessonStatus current, LessonStatus next) {
        return switch (current) {
            case SCHEDULED -> next == LessonStatus.IN_PROGRESS || next == LessonStatus.CANCELED;
            case IN_PROGRESS -> next == LessonStatus.COMPLETED || next == LessonStatus.CANCELED;
            case COMPLETED, CANCELED -> false;
        };
    }
}
