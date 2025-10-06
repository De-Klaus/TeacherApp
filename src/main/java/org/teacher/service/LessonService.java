package org.teacher.service;

import org.teacher.dto.LessonDto;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    LessonDto addLesson(LessonDto lessonDto);
    Optional<LessonDto> getById(Long lessonId);
    List<LessonDto> getAll();
    LessonDto update(Long lessonId, LessonDto dto);
    void delete(Long lessonId);
}
