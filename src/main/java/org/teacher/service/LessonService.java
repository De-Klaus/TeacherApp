package org.teacher.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.teacher.dto.LessonDto;
import org.teacher.entity.LessonStatus;

import java.util.Optional;

public interface LessonService {
    LessonDto addLesson(LessonDto lessonDto);
    Optional<LessonDto> getById(Long lessonId);
    Page<LessonDto> getAll(Pageable pageable);
    LessonDto update(Long lessonId, LessonDto dto);
    void delete(Long lessonId);
    LessonDto updateLessonStatus(Long lessonId, LessonStatus lessonStatus);
}
