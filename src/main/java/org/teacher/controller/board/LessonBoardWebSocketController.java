package org.teacher.controller.board;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.teacher.service.LessonBoardService;

@Controller
@RequiredArgsConstructor
public class LessonBoardWebSocketController {

    private final LessonBoardService lessonBoardService;

    @MessageMapping("/lessons/{lessonId}/board")
    @SendTo("/topic/lessons/{lessonId}/board")
    public String handleBoardUpdate(@DestinationVariable Long lessonId, String sceneJson) {
        // Сохранение и рассылка обновлений
        var saveResult = lessonBoardService.saveBoard(lessonId, sceneJson);
        return saveResult.sceneJson();
    }
}
