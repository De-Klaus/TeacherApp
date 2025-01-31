package org.teacher.controller;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.model.Lesson;
import org.teacher.service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/{userId}")
    public ResponseEntity<Lesson> addLesson(@PathVariable Long userId, @RequestBody Lesson lesson) {
        return ResponseEntity.ok(lessonService.addLesson(userId, lesson));
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<List<Object[]>> getSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(lessonService.getSummary(userId));
    }
}
