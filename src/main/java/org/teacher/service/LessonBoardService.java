package org.teacher.service;

import org.teacher.dto.LessonBoardDto;

public interface LessonBoardService {

    LessonBoardDto saveBoard(Long lessonId, String sceneJson);

    String loadBoard(Long lessonId);

    boolean boardExists(Long lessonId);
}
