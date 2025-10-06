package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.LessonDto;
import org.teacher.mapper.LessonMapper;
import org.teacher.service.LessonService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@RequestBody @Valid LessonDto lessonDto) {
        LessonDto responseDto = lessonService.addLesson(lessonDto);
        return ResponseEntity
                .created(URI.create("/lessons/" + responseDto.lessonId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long lessonId) {
        return lessonService.getById(lessonId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long lessonId, @RequestBody @Valid LessonDto dto) {
        return ResponseEntity.ok(lessonService.update(lessonId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.delete(lessonId);
        return ResponseEntity.noContent().build();
    }
}
