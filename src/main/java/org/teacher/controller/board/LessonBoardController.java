package org.teacher.controller.board;

import lombok.RequiredArgsConstructor;
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
        lessonBoardService.saveBoard(lessonId, sceneJson);
        return ResponseEntity.ok("Saved");
    }

    @GetMapping("/load")
    public ResponseEntity<String> loadBoard(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonBoardService.loadBoard(lessonId));
    }
}
