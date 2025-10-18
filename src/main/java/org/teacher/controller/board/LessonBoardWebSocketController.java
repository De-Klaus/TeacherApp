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

    @MessageMapping("/lesson/{lessonId}/update")  // сообщения приходят сюда
    @SendTo("/topic/lesson/{lessonId}")          // все подписчики получат данные
    public String updateBoard(@DestinationVariable Long lessonId, String sceneJson) {
        // Можно одновременно сохранять состояние на сервер
        lessonBoardService.saveBoard(lessonId, sceneJson);
        return sceneJson;
    }
}
