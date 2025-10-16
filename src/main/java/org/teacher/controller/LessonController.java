package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teacher.common.dto.PageRequestDto;
import org.teacher.common.dto.PageResponseDto;
import org.teacher.dto.LessonDto;
import org.teacher.entity.LessonStatus;
import org.teacher.service.LessonService;

import java.net.URI;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@RequestBody @Valid LessonDto lessonDto) {
        LessonDto responseDto = lessonService.addLesson(lessonDto);
        return ResponseEntity
                .created(URI.create("/lessons/" + responseDto.id()))
                .body(responseDto);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable("id") Long lessonId) {
        return lessonService.getById(lessonId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponseDto<LessonDto>> getAllLessons(PageRequestDto pageRequest) {
        Page<LessonDto> page = lessonService.getAll(pageRequest.toPageable());
        return ResponseEntity.ok(PageResponseDto.from(page));
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable("id") Long lessonId, @RequestBody @Valid LessonDto dto) {
        return ResponseEntity.ok(lessonService.update(lessonId, dto));
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable("id") Long lessonId) {
        lessonService.delete(lessonId);
        return ResponseEntity.noContent().build();
    }

    // ===== Методы для изменения статуса =====

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PatchMapping("/{id}/start")
    public ResponseEntity<LessonDto> startLesson(@PathVariable Long id) {
        LessonDto lesson = lessonService.updateLessonStatus(id, LessonStatus.IN_PROGRESS);
        return ResponseEntity.ok(lesson);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<LessonDto> completeLesson(@PathVariable Long id) {
        LessonDto lesson = lessonService.updateLessonStatus(id, LessonStatus.COMPLETED);
        return ResponseEntity.ok(lesson);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LessonDto> cancelLesson(@PathVariable Long id) {
        LessonDto lesson = lessonService.updateLessonStatus(id, LessonStatus.CANCELED);
        return ResponseEntity.ok(lesson);
    }
}
