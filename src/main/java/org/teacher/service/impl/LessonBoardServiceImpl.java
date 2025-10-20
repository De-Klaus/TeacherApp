package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.dto.LessonBoardDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonBoard;
import org.teacher.mapper.LessonBoardMapper;
import org.teacher.mapper.LessonMapper;
import org.teacher.repository.LessonBoardRepository;
import org.teacher.service.LessonBoardService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonBoardServiceImpl implements LessonBoardService {

    private final LessonBoardRepository lessonBoardRepository;
    private final LessonBoardMapper lessonBoardMapper;

    @Override
    public LessonBoardDto saveBoard(Long lessonId, String sceneJson) {
        LessonBoard board = lessonBoardRepository.findByLesson_LessonId(lessonId)
                .orElseGet(() -> LessonBoard.builder()
                        .lesson(
                                Lesson.builder().lessonId(lessonId).build()
                        ).build());
        board.setSceneJson(sceneJson);
        board.setUpdatedAt(LocalDateTime.now());
        return lessonBoardMapper.toDto(lessonBoardRepository.save(board));
    }

    @Override
    public String loadBoard(Long lessonId) {
        return lessonBoardRepository.findByLesson_LessonId(lessonId)
                .map(LessonBoard::getSceneJson)
                .orElse("[]");
    }

    @Override
    public boolean boardExists(Long lessonId) {
        return lessonBoardRepository
                .findByLesson_LessonId(lessonId)
                .isPresent();
    }
}
