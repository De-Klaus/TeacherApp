package org.teacher.controller.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.service.LessonBoardService;

@RestController
@RequestMapping("/api/lessons/{lessonId}/board")
@RequiredArgsConstructor
public class LessonBoardController {

    private final LessonBoardService lessonBoardService;

    @PostMapping("/save")
    public ResponseEntity<String> saveBoard(@PathVariable Long lessonId,
                                            @RequestBody String sceneJson) {
        try {
            lessonBoardService.saveBoard(lessonId, sceneJson);
            return ResponseEntity.ok("Board saved successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lesson not found: " + lessonId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving board: " + e.getMessage());
        }
    }

    @GetMapping("/load")
    public ResponseEntity<String> loadBoard(@PathVariable Long lessonId) {
        try {
            String sceneJson = lessonBoardService.loadBoard(lessonId);
            return ResponseEntity.ok(sceneJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("[]");
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> boardExists(@PathVariable Long lessonId) {
        boolean exists = lessonBoardService.boardExists(lessonId);
        return ResponseEntity.ok(exists);
    }
}
