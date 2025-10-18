package org.teacher.service;

import org.teacher.dto.LessonBoardDto;

public interface LessonBoardService {

    public LessonBoardDto saveBoard(Long lessonId, String sceneJson);

    public String loadBoard(Long lessonId);
}
