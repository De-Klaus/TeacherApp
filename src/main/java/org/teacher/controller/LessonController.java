package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teacher.common.dto.PageResult;
import org.teacher.dto.LessonDto;
import org.teacher.service.LessonService;

import java.net.URI;
import java.util.List;

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
                .created(URI.create("/lessons/" + responseDto.lessonId()))
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
    public ResponseEntity<PageResult<LessonDto>> getAllLessons(Pageable pageable) {
        Page<LessonDto> page = lessonService.getAll(pageable);

        PageResult<LessonDto> response = new PageResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isLast()
        );

        return ResponseEntity.ok(response);
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
}
